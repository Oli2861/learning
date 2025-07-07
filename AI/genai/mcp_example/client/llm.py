import logging
import os
from typing import Dict, List, Optional, Tuple, Type, TypeVar

import tiktoken
from openai import AsyncAzureOpenAI, AzureOpenAI
from openai.types.chat import ChatCompletion, ChatCompletionMessageToolCall, ParsedFunctionToolCall
from openai.types.chat.chat_completion import Choice
from pydantic import BaseModel

from message import Message


class LLM:
    client: AzureOpenAI
    TAG = "LLM"

    def __init__(self) -> None:
        client, client_async = self.get_clients()
        self.client = client
        self.client_async = client_async
        self.deployment_name = os.getenv("AZURE_OPENAI_MODEL", "gpt-4o")

    def get_clients(self) -> Tuple[AzureOpenAI, AsyncAzureOpenAI]:
        endpoint = os.getenv("AZURE_OPENAI_ENDPOINT")
        assert (
            endpoint is not None
        ), "The OPENAI_API_ENDPOINT environment variable must be set."
        api_key = os.getenv("AZURE_OPENAI_KEY")
        assert (
            api_key is not None
        ), "The OPENAI_API_KEY environment variable must be set."
        api_version = os.getenv("AZURE_OPENAI_API_VERSION")
        assert (
            api_version is not None
        ), "The OPENAI_API_VERSION environment variable must be set."

        aoi_client = AzureOpenAI(
            api_key=os.getenv("AZURE_OPENAI_KEY"),
            api_version="2024-10-21",
            azure_endpoint=endpoint,
            timeout=120,
            max_retries=5,
        )
        aoi_async_client = AsyncAzureOpenAI(
            api_key=os.getenv("AZURE_OPENAI_KEY"),
            api_version="2024-10-21",
            azure_endpoint=endpoint,
            timeout=120,
            max_retries=5,
        )

        self.encoding = {}
        self.encoding["gpt-4o"] = tiktoken.encoding_for_model("gpt-4o")
        return (aoi_client, aoi_async_client)

    T = TypeVar("T", bound=BaseModel)

    def _count_tokens(self, chat_history: List[Message], model_name: str) -> int:
        contents: List[str] = []
        for msg in chat_history:
            for content in msg.content:
                if content.text is not None:
                    contents.append(content.text)
                else:
                    logging.info(
                        f"_count_tokens: Probably encountered an image: {content.text=} {content.content_type=}.",
                        extra={"TAG": self.TAG},
                    )

        encoding = self.encoding.get(model_name)
        if encoding is None:
            encoding = tiktoken.encoding_for_model(model_name)
            self.encoding[model_name] = encoding

        tokens = encoding.encode_batch(contents)
        counts = [len(ts) for ts in tokens]
        token_count = sum(counts)
        return token_count

    async def structured_output_async(
        self,
        chat_history: List[Message],
        response_format: Type[T],
        count_tokens: bool = False,
    ) -> Optional[T]:
        messages = [it.my_model_dump() for it in chat_history]

        if count_tokens:
            token_count = self._count_tokens(chat_history, self.deployment_name)
            logging.info(
                f"{token_count=}",
                extra={"TAG": self.TAG},
            )

        response: ChatCompletion = await self.client_async.beta.chat.completions.parse(
            model=self.deployment_name,
            messages=messages,  # pyright: ignore [reportArgumentType]
            response_format=response_format,
        )
        result = response.choices[0].message.parsed
        return result

    async def chat_async(
        self,
        chat_history: List[Message],
        functions: List[Dict],
        count_tokens: bool = False,
    ) -> Tuple[Optional[str], Optional[List[ParsedFunctionToolCall]]]:
        messages = [it.my_model_dump() for it in chat_history]

        if count_tokens:
            # Assuming deployment name = model name
            token_count = self._count_tokens(chat_history, self.deployment_name)
            logging.info(
                f"{token_count=}",
                extra={"TAG": self.TAG},
            )

        logging.info(
            f"LLM.chat_async: messages={messages}, functions={functions}",
            extra={"TAG": self.TAG},
        )
        response: ChatCompletion = await self.client_async.beta.chat.completions.parse(
            model=self.deployment_name,
            messages=messages,  # pyright: ignore [reportArgumentType]
            tools=functions,
        )
        first_choice: Choice = response.choices[0]
        first_msg_content: Optional[str] = first_choice.message.content
        tool_calls: Optional[List[ParsedFunctionToolCall]] = first_choice.message.tool_calls

        return first_msg_content, tool_calls
