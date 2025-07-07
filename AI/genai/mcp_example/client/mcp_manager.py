import logging
from typing import Any, Dict, Iterable, List, Optional, Tuple

from anyio.streams.memory import MemoryObjectReceiveStream, MemoryObjectSendStream
from mcp import (
    ClientSession,
    ListToolsResult,
    ReadResourceResult,
    Resource,
    Tool,
    types,
)
from mcp.client.sse import sse_client
from mcp.shared.message import SessionMessage
from pydantic import AnyUrl, BaseModel

logging.basicConfig(format="%(asctime)s %(message)s", level=logging.DEBUG, force=True)


# Optional: create a sampling callback
async def handle_sampling_message(
    message: types.CreateMessageRequestParams,
) -> types.CreateMessageResult:
    return types.CreateMessageResult(
        role="assistant",
        content=types.TextContent(
            type="text",
            text="Hello, world! from model",
        ),
        model="gpt-3.5-turbo",
        stopReason="endTurn",
    )


class MCPServer(BaseModel):
    name: str
    url: str


class MCPManagerException(Exception):
    pass


class SSEServerRegistrationException(MCPManagerException):
    pass


class MCPToolConverterException(MCPManagerException):
    pass


class MCPToolNotFoundException(MCPManagerException):
    pass


class MCPManager:
    TAG = "MCPManager"

    client_transports: Dict[str, Any]
    sessions: Dict[str, ClientSession]

    def __init__(self) -> None:
        self.client_transports = {}
        self.sessions = {}

    # --- Adding & removing servers: Session management --- #
    async def add_mcp_server(self, name: str, url: str):
        client = sse_client(url)

        # The SSE session is a Asynchronous Context Manager and has a Receive and Write stream.

        streams: Tuple[
            MemoryObjectReceiveStream[SessionMessage | Exception],
            MemoryObjectSendStream[SessionMessage],
        ] = await client.__aenter__()
        receive_stream: MemoryObjectReceiveStream[SessionMessage | Exception] = streams[
            0
        ]
        send_stream: MemoryObjectSendStream[SessionMessage] = streams[1]

        session: ClientSession = ClientSession(receive_stream, send_stream)
        session = await session.__aenter__()

        logging.info(f"{self.TAG}.add_mcp_server: Initializing session..")

        init_result = await session.initialize()
        logging.info(
            f"{self.TAG}.add_mcp_server: Initialized {init_result}. Sending ping.."
        )

        self.client_transports[name] = client
        self.sessions[name] = session

        response = await session.send_ping()
        if isinstance(response, types.EmptyResult):
            logging.info(f"{self.TAG}.add_mcp_server: Received ping response")
        else:
            raise SSEServerRegistrationException("The MCP Server did not respond.")

    async def remove_mcp_server(self, name: str, exc_type=None, exc=None, tb=None):
        session = self.sessions.pop(name)
        await session.__aexit__(exc_type, exc, tb)
        client = self.client_transports.pop(name)
        await client.__aexit__(exc_type, exc, tb)

    async def remove_all_mcp_servers(self, exc_type=None, exc=None, tb=None):
        for name in list(self.sessions.keys()):
            await self.remove_mcp_server(name, exc_type, exc, tb)

    async def __aenter__(self):
        logging.debug(f"{self.TAG}.__aenter__: Initializing MCPManager")
        return self

    async def __aexit__(self, exc_type, exc, tb):
        logging.debug(f"{self.TAG}.__aexit__: Cleaning up MCPManager")
        await self.remove_all_mcp_servers(exc_type, exc, tb)

    # --- Using the clients --- #

    async def get_resources(self) -> Dict[str, List[Resource]]:
        resources: Dict[str, List[Resource]] = {}
        for session_name, session in self.sessions.items():
            result = await session.list_resources()
            resources[session_name] = result.resources

        logging.debug(f"{self.TAG}.get_resources: {resources}")
        return resources

    async def get_resource(
        self, server_name: str, resource_url: AnyUrl
    ) -> List[types.TextResourceContents | types.BlobResourceContents]:
        if server_name not in self.sessions:
            raise MCPManagerException(f"Server {server_name} not found.")

        session = self.sessions[server_name]
        result: ReadResourceResult = await session.read_resource(resource_url)

        contents: List[types.TextResourceContents | types.BlobResourceContents] = (
            result.contents
        )
        # logging.debug(f"{self.TAG}.get_resource: {contents=}")
        return result.contents

    async def get_tools(self) -> Iterable[Tool]:
        tools: List[Tool] = []
        for session in self.sessions.values():
            result = await session.list_tools()
            tools += result.tools

        # logging.debug(f"{self.TAG}.get_tools: {tools}")
        return tools

    async def get_function_calling_specs(self) -> Iterable[Dict[str, Any]]:
        tools = await self.get_tools()
        return self.convert_to_function_calling_spec_many(tools)

    async def get_session_with_tool(self, name: str) -> ClientSession:
        discovered_tools = []
        for session in self.sessions.values():
            ltr: ListToolsResult = await session.list_tools()
            tools: List[Tool] = ltr.tools
            discovered_tools += tools
            for tool in tools:
                if tool.name == name:
                    return session

        raise MCPToolNotFoundException(
            f"Didnt find a tool named {name}. Available tools: {[it.name for it in discovered_tools]}"
        )

    async def call_tool(self, name: str, args: Dict[str, Any]) -> types.CallToolResult:
        session = await self.get_session_with_tool(name)
        result: types.CallToolResult = await session.call_tool(name, args)
        if result.isError:
            logging.error(f"{self.TAG}.call_tool: {result.isError=} {result.content}")
        else:
            logging.debug(f"{self.TAG}.call_tool: {result.isError=} {result.content}")
        return result

    @staticmethod
    def convert_to_function_calling_spec_many(
        tools: Iterable[Tool],
    ) -> Iterable[Dict[str, Any]]:
        return [MCPManager.convert_to_function_calling_spec(tool) for tool in tools]

    @staticmethod
    def convert_to_function_calling_spec(tool: Tool) -> Dict[str, Any]:
        input_schema: Dict[str, Any] = tool.inputSchema
        required_args: Optional[List[str]] = input_schema.get("required", None)
        properties: Optional[Dict[str, str]] = input_schema.get("properties", None)

        function_schema: Dict[str, Any] = {
            "type": "function",
            "function": {
                "name": tool.name,
                "description": tool.description if tool.description else "",
                "parameters": {
                    "type": "object",
                    "properties": properties if properties else {},
                    "required": required_args if required_args else [],
                    "additionalProperties": False,
                },
                "strict": True,
            },
        }
        return function_schema
