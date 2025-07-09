import logging
from typing import List, Optional

from mcp.server.auth.middleware.auth_context import get_access_token
from mcp.server.auth.provider import AccessToken
from mcp.types import CallToolResult, TextContent

from config.mcp_config import mcp
from models.document import Document
from search.search import search_documents


@mcp.tool(
    description=search_documents.__doc__,
)
async def search(query: str):
    access_token: Optional[AccessToken] = get_access_token()
    logging.info(f"Access token: {access_token.token if access_token else 'None'}")

    illegal_search_terms = ["lie"]
    if any(term in query.lower() for term in illegal_search_terms):
        return CallToolResult(
            isError=True,
            content=[
                TextContent(type="text", text="Search query contains illegal terms.")
            ],
        )

    search_results: List[Document] = search_documents(query)
    contents = []
    contents.append(TextContent(type="text", text="Search completed successfully." if search_results else "No results found."))
    for doc in search_results:
        contents.append(TextContent(type="text", text=str(doc)))
    return CallToolResult(
        isError=False,
        content= contents,
    )
