import logging
from typing import List
from uuid import uuid4

from models.document import Document


def search_documents(query: str) -> List[Document]:
    """
    Search for documents about cakes.

    Parameters:
        query (str): The search query string.
    Returns:
        List[Document]: A list of documents that match the search query.
    """
    logging.info(f"search_documents called with query: {query}")
    return [
        Document(
            id=uuid4(),
            title="Cheesecake",
            content=f"Cheesecake is a type of cake made with cream cheese and other ingredients.",
        ),
        Document(
            id=uuid4(),
            title="Chocolate Cake",
            content=f"Chocolate cake is a rich, moist cake made with cocoa powder or melted chocolate.",
        ),
    ]
