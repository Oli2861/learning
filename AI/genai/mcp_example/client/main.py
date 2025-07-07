import asyncio
import json
import logging
from typing import Iterable, Dict, List

from mcp import Resource, Tool, types
from pydantic import AnyUrl

from mcp_manager import MCPManager


async def main() -> None:
    async with MCPManager() as mcp_manager:
        await mcp_manager.add_mcp_server("SearchServer", "http://localhost:8000/sse")

        tools: Iterable[Tool] = await mcp_manager.get_tools()
        logging.info(f"Discovered {len(list(tools))} tools:")

        # Print the tools in a readable format
        for tool in tools:
            logging.info(f"\n---\nTool {tool.name}:\n{tool.model_dump_json()}\n---\n")

        # Print the resources in a readable format
        resources: Dict[str, List[Resource]] = await mcp_manager.get_resources()
        for sever_name, resource in resources.items():
            logging.info(f"Resources for {sever_name}:")
            for res in resource:
                logging.info(f"    - {res.name} - {res.model_dump_json()}")

        # Example of reading a resource
        uri = AnyUrl("file:///files/cakes/Gedeckter_Apfelkuchen.md")
        contents: List[types.TextResourceContents | types.BlobResourceContents] = await mcp_manager.get_resource("SearchServer", uri)
        logging.info(f"Contents of {uri}:\n{json.dumps([content.model_dump_json() for content in contents], indent=2)}")

        await asyncio.sleep(5)


if __name__ == "__main__":

    logging.basicConfig(level=logging.INFO)
    asyncio.run(main())
