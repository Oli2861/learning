package com.oli.event

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


object EventSerializer {

    private val format = Json {
        serializersModule = SerializersModule {
            polymorphic(Event::class) {
                subclass(AuthorizationCommandEvent::class, AuthorizationCommandEvent.serializer())
                subclass(AuthorizationReplyEvent::class, AuthorizationReplyEvent.serializer())
            }
        }
    }

    fun deserialize(event: String): Event = format.decodeFromString(event)
    fun serialize(event: Event): String = format.encodeToString(event)

}