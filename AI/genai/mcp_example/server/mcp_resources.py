import logging
import os
from typing import List

from mcp.types import Resource
from pydantic import AnyUrl

from mcp_config import mcp


@mcp._mcp_server.list_resources()
async def list_resources() -> List[Resource]:
    """
    List all available resources.
    """
    resources: List[Resource] = []
    for path, subdirs, files in os.walk("./files/"):
        for name in files:
            path: str = os.path.join(path, name)
            uri = AnyUrl.build(
                scheme="file",
                path=path,
                host="",
            )
            resource = Resource(
                name=name,
                uri=uri,
                mimeType="text/plain",
            )
            resources.append(resource)

    return resources


def read_file(file_path: str) -> str:
    """
    Read the content of a file.
    """
    path = file_path.lstrip("/")
    absolute_path = os.getcwd()
    path = os.path.join(absolute_path, path)
    logging.info(f"Reading resource from path: {path}")

    if not os.path.exists(path):
        logging.error(f"File not found: {path}")
        raise FileNotFoundError(f"Resource not found: {path}")

    with open(path, "r", encoding="utf-8") as file:
        return file.read()


@mcp._mcp_server.read_resource()
async def read_resource(uri: AnyUrl) -> str:
    """
    Read the content of a resource.
    """
    file_path = uri.path
    if file_path is None:
        raise ValueError("Invalid URI: path is None")

    try:
        content = read_file(file_path)
    except FileNotFoundError as e:
        logging.debug(f"Error reading resource: {e}")
        raise e
    logging.info(f"Read content from {file_path}")
    return content
