import logging
from typing import List

from fastapi import FastAPI, HTTPException
from starlette.responses import JSONResponse
from starlette.routing import BaseRoute

from search_router import search_router

app = FastAPI()


def configure_fastapi(
    mcp_routes: List[BaseRoute],
) -> FastAPI:
    app.router.include_router(search_router)
    app.router.routes += mcp_routes

    # @app.exception_handler(Exception)
    # async def catch_all_exceptions(request, exc):
    #     logging.error(f"Unexpected error: {str(exc)}")
    #     return JSONResponse(
    #         status_code=500,
    #         content={"detail": "Internal Server Error"}
    #     )

    # @app.exception_handler(HTTPException)
    # async def http_exception_handler(request, exc):
    #     logging.error(f"HTTP Exception: {exc.status_code} - {exc.detail}")
    #     return JSONResponse(
    #         status_code=exc.status_code,
    #         content={"detail": str(exc.detail)}
    #     )
    return app
