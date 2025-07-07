import logging
from typing import Callable, List

from mcp.server.fastmcp import FastMCP
from mcp.server.sse import SseServerTransport
from starlette.requests import Request
from starlette.routing import BaseRoute, Mount, Route

mcp = FastMCP("KDP")


def configure_mcp(
    tool_definitions: List[Callable], resource_definitions: List[Callable]
) -> List[BaseRoute]:

    sse = SseServerTransport("/messages/")
    logging.info(
        f"Found {len(tool_definitions)} tools and {len(resource_definitions)} resources."
    )

    async def handle_sse_request(request: Request) -> None:
        async with sse.connect_sse(
            request.scope,
            request.receive,
            request._send,
        ) as (read_stream, write_stream):

            await mcp._mcp_server.run(
                read_stream,
                write_stream,
                mcp._mcp_server.create_initialization_options(),
            )

    routes: List[BaseRoute] = [
        Route("/sse", endpoint=handle_sse_request),
        Mount("/messages/", app=sse.handle_post_message),
    ]
    return routes
