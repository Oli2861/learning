import uvicorn

from fastapi_config import configure_fastapi
from mcp_config import configure_mcp
from mcp_resources import list_resources, read_file
from mcp_tools import search

if __name__ == "__main__":
    mcp_routes = configure_mcp(
        tool_definitions=[search], resource_definitions=[list_resources, read_file]
    )
    app = configure_fastapi(mcp_routes)
    uvicorn.run(app=app, host="0.0.0.0", port=8000, log_level="info", workers=1)
