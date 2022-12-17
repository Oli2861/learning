package com.oli.customer

import kotlinx.serialization.Serializable

// Annotate classes with serializable for kotlinx serialization
@Serializable
data class Customer(val id: String, val firstName: String, val lastName: String, val email: String)
