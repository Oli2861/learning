package com.oli.payment

import kotlinx.serialization.Serializable

@Serializable
data class CreditCardInfo(
    val userId: Int,
    val info: String
)
