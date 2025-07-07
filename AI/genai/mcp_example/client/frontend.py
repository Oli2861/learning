import asyncio
import logging
from typing import List

import streamlit as st

from agent import MCPAgent
from llm import LLM
from mcp_manager import MCPManager, MCPServer
from message import Message, Role, TextMessageContent, ToolCallResponseMessage

servers = [MCPServer(name="SearchServer", url="http://localhost:8000/sse")]
llm: LLM = LLM()


def draw_messages(messages: List[Message]) -> None:
    # logging.info("Drawing messages in chat history")
    for message in messages:
        # logging.info(f"Message: {message}")
        assert isinstance(
            message, Message
        ), "Chat history should contain Message instances"

        role_str: str
        if isinstance(message.role, Role):
            role_str = message.role.value
        else:
            role_str = message.role

        with st.chat_message(role_str):
            st.markdown(
                "\n".join(
                    content.text
                    for content in message.content
                    if isinstance(content, TextMessageContent)
                    and content.text is not None
                )
            )
            if message.tool_calls:
                for tool_call in message.tool_calls:
                    st.markdown(f"{tool_call.function.name=}\n\n{tool_call.id=}\n\n{tool_call.function.arguments}")
            if isinstance(message, ToolCallResponseMessage):
                for response in message.responses:
                    st.markdown(
                        f"{response.name=}\n\n{response.tool_call_id=}:\n\n{response.output}"
                    )


if st.session_state.get("chat_history") is None:
    st.session_state["chat_history"] = [Message(role=Role.SYSTEM, content=[TextMessageContent(text="Instructions: You are a helpful assistant. If you are talking about cake, only mention cakes you know from the search tool.")])]

chat_read = st.session_state.get("chat_history", None)
assert chat_read is not None, "Chat history should not be None"
draw_messages(chat_read)


async def on_submit() -> None:
    async with MCPManager() as mcp_manager:
        agent = MCPAgent(llm, mcp_manager)
        messages = st.session_state["chat_history"]
        updated_history: List[Message] = await agent(messages)
        st.session_state["chat_history"] = updated_history
        st.rerun()


# Accept user input
if user_input := st.chat_input("Type your message here..."):
    # Display user message in chat message container
    with st.chat_message("user"):
        st.markdown(user_input)
    # Add user message to chat history
    st.session_state["chat_history"].append(
        Message(role=Role.USER, content=[TextMessageContent(text=user_input)])
    )
    asyncio.run(on_submit())
