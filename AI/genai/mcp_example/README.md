A simple python example about the [Model Context Protocol (MCP)](https://modelcontextprotocol.io/introduction).

Relevant files:
- `server/main.py`: A simple FastAPI server that uses the MCP.
    - Uses the `configure_mcp` function to obtain the MCP routes.
    - Uses the `configure_fastapi` function to obtain a FastAPI app with the MCP routes.
    - Runs a uvicorn server to serve the FastAPI app.
- `server/mcp_config.py`: How to configure mcp & get routes that can be added to a FastAPI app (done in `server/fastapi_config.py`).
- `server/mcp_tools.py`: Contains the tools that are used in the MCP.
    - Uses the doc from the `server/search.py` function as a description for the `search` MCP-tool.
- `server/mcp_resources.py`: Contains resources, in this case the files in `server/files/`.


[Blog post about MCP](https://quartz.oliver-schmid.net/LLMs/Model-Context-Protocol-MCP)
