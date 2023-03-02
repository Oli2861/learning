package com.oli.event

import com.oli.customer.Customer
import kotlinx.serialization.Serializable

@Serializable
data class VerifyCustomerEvent(val customerId: Int, val customer: Customer)
@Serializable
data class VerifyCustomerEventResult(val customerId: Int, val customer: Customer, val result: Boolean?)
