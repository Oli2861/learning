import json
import logging
from typing import Any, Dict, List, Optional, Tuple

from mcp.types import CallToolResult, TextContent
from openai.types.chat import ChatCompletionMessageToolCall, ParsedFunctionToolCall

from llm import LLM
from mcp_manager import MCPManager
from message import (
    FunctionSpec,
    Message,
    Role,
    TextMessageContent,
    ToolCall,
    ToolCallResponseMessage,
    ToolResponse,
)


class MCPAgentException(Exception):
    pass


class MCPAgentMaxIterationsReachedException(Exception):
    pass


class MCPUnconsideredResponseTypeException(MCPAgentException):
    pass


class MCPErrorException(MCPAgentException):
    pass


class MCPAgent:
    TAG: str = "MCPAgent"
    manager: MCPManager
    llm: LLM
    MAX_ITERATIONS: int = 10

    def __init__(self, llm: LLM, manager: MCPManager) -> None:
        self.manager = manager
        self.llm = llm

    def process_result(
        self, result: CallToolResult, tool_call: ParsedFunctionToolCall
    ) -> ToolCallResponseMessage:
        if result.isError:
            raise MCPErrorException(f"Encountered an error: {result=}")

        text_contents = []
        for content in result.content:
            if isinstance(content, TextContent):
                text_contents.append(content.text)
            else:
                raise MCPUnconsideredResponseTypeException(
                    f"Received a response with a unconsidered content type: {type(content)=} {content=}"
                )

        msg = ToolCallResponseMessage(
            role=Role.TOOL,
            tool_call_id=tool_call.id,
            responses=[
                ToolResponse(
                    tool_call_id=tool_call.id,
                    name=tool_call.function.name,
                    output="".join(text_contents),
                )
            ],
        )
        logging.error(f"{self.TAG}.process_result")
        return msg

    async def __call__(self, chat_history: List[Message]) -> List[Message]:
        history = chat_history.copy()
        await self.manager.add_mcp_server(
            name="search server", url="http://localhost:8000/sse"
        )
        functions: List[Dict[str, Any]] = list(
            await self.manager.get_function_calling_specs()
        )

        for _ in range(0, self.MAX_ITERATIONS):
            logging.error(
                f"{self.TAG}.__call__: Iteration {_ + 1}/{self.MAX_ITERATIONS}"
            )
            # for msg in history:
                # logging.error(f"{self.TAG}.__call__: {msg}")
            response: Tuple[Optional[str], Optional[List[ParsedFunctionToolCall]]] = (
                await self.llm.chat_async(history, functions)
            )
            (text, tool_calls) = response

            if tool_calls is not None:
                first_tool_call: ChatCompletionMessageToolCall = tool_calls[0]
                json_as_dict: Dict[str, Any] = json.loads(
                    first_tool_call.function.arguments
                )
                history.append(
                    Message(
                        role=Role.ASSISTANT,
                        content=[],
                        tool_calls=[
                            ToolCall(
                                id=first_tool_call.id,
                                function=FunctionSpec(
                                    name=first_tool_call.function.name,
                                    arguments=first_tool_call.function.arguments,
                                ),
                            )
                        ],
                    )
                )
                result: CallToolResult = await self.manager.call_tool(
                    name=first_tool_call.function.name, args=json_as_dict
                )
                
                tool_call_response_msg: ToolCallResponseMessage = self.process_result(result, first_tool_call)
                history.append(tool_call_response_msg)

                system_message = Message(
                    role=Role.SYSTEM,
                    content=[
                        TextMessageContent(
                            text=f"You are a helpful assistant. If you are talking about cake, only mention cakes you know from the search tool. New information from the search tool: {', '.join([response.output for response in tool_call_response_msg.responses])}"
                        )
                    ],
                )
                history.append(system_message)

            if text is not None:
                await self.manager.__aexit__(None, None, None)
                logging.error(
                    f"{self.TAG}.__call__: Concluded with the following response: {text}"
                )
                history.append(
                    Message(
                        role=Role.ASSISTANT,
                        content=[TextMessageContent(text=text)],
                    )
                )
                return history

        await self.manager.__aexit__(None, None, None)
        # logging.error(f"{self.TAG}.__call__: {history}")
        raise MCPAgentMaxIterationsReachedException(
            f"The agent didnt come to a conclusion after {self.MAX_ITERATIONS}."
        )
