from uuid import UUID

from pydantic import BaseModel


class Document(BaseModel):
    id: UUID
    title: str
    content: str

    def __str__(self):
        return f"**{self.title}**\n{self.content}"
