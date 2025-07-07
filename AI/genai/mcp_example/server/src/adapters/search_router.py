from typing import Any, Dict, List

from fastapi import APIRouter

from models.document import Document
from models.search_request import SearchRequest
from search.search import search_documents

search_router: APIRouter = APIRouter()


@search_router.post("/search")
async def search(request: SearchRequest) -> Dict[str, Any]:
    docs: List[Document] = search_documents(request.query)
    return {"query": request.query, "results": [doc.model_dump() for doc in docs]}
