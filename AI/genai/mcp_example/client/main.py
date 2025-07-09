import asyncio
import json
import logging
from typing import Iterable, List

from mcp import ClientSession, Resource, Tool, types
from mcp.client.sse import sse_client
from pydantic import AnyUrl


async def main() -> None:
    async with sse_client("http://localhost:8000/sse") as (read, write):
        async with ClientSession(read, write) as session:
            await session.initialize()

            tools: Iterable[Tool] = (await session.list_tools()).tools
            logging.info(f"Discovered {len(list(tools))} tools:")

            # Print the tools in a readable format
            for tool in tools:
                logging.info(f"\n---\nTool {tool.name}:\n{tool.model_dump_json(indent=4)}\n---\n")

            # Print the resources in a readable format
            resources: List[Resource] = (await session.list_resources()).resources
            for res in resources:
                logging.info(f"- {res.name} - {res.model_dump_json(indent=4)}")

            # Example of reading a resource
            uri = AnyUrl("file:///files/cakes/Gedeckter_Apfelkuchen.md")
            contents: List[types.TextResourceContents | types.BlobResourceContents] = (await session.read_resource(uri)).contents
            logging.info(f"Contents of {uri}:\n{json.dumps([content.model_dump_json() for content in contents], indent=4)}")


if __name__ == "__main__":

    logging.basicConfig(level=logging.INFO)
    asyncio.run(main())
