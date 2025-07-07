import enum
from typing import Any, Dict, List, Optional

from pydantic import BaseModel, Field, model_validator


class Role(str, enum.Enum):
    SYSTEM = "system"
    USER = "user"
    ASSISTANT = "assistant"
    TOOL = "tool"


class ContentType(str, enum.Enum):
    TEXT = "text"
    IMAGE_URL = "image_url"


class MessageContent(BaseModel):
    class Config:
        use_enum_values = True

    content_type: ContentType = Field(
        serialization_alias="type", default=ContentType.TEXT
    )
    text: Optional[str] = Field(default=None)
    image_url: Optional[Dict[str, str]] = Field(default=None)

    def __hash__(self):
        return hash((self.content_type, self.text, self.image_url))


class B64ImageMessageContent(MessageContent):
    def __init__(self, b64img: str):
        return super().__init__(
            content_type=ContentType.IMAGE_URL,
            text=None,
            image_url={"url": f"data:image/png;base64,{b64img}"},
        )


class TextMessageContent(MessageContent):
    def __init__(self, text: str):
        return super().__init__(
            content_type=ContentType.TEXT,
            text=text,
        )

    def __hash__(self):
        return hash((self.text, self.content_type))

class FunctionSpec(BaseModel):
    name: str
    arguments: str

class ToolCall(BaseModel):
    type: str = Field(
        default="function", description="Type of the tool call."
    )
    id: str = Field(..., description="ID of the tool call.")
    function: FunctionSpec

class Message(BaseModel):
    class Config:
        use_enum_values = True

    role: Role
    content: List[MessageContent] = Field(
        default_factory=list,
        description="List of message content items.",
    )
    tool_calls: Optional[List[ToolCall]] = Field(
        default=None,
        description="List of tool calls associated with the message.",
    )

    def my_model_dump(self):
        return self.model_dump(exclude_none=True, by_alias=True)

class ToolResponse(BaseModel):
    tool_call_id: str = Field(..., description="ID of the tool response.")
    name: str = Field(..., description="Name of the tool that produced the response.")
    output: str

class ToolCallResponseMessage(Message):
    tool_call_id: str = Field(..., description="ID of the tool call.")
    role: Role = Field(default=Role.TOOL)
    responses: List[ToolResponse] = Field()
