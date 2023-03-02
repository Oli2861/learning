package com.oli.proxies

import kotlinx.serialization.Serializable

interface CustomerServiceProxy{
    fun verifyConsumerDetails(sagaId: Int): Boolean
}
class CustomerServiceProxyImpl: CustomerServiceProxy{
    override fun verifyConsumerDetails(sagaId: Int): Boolean {
        return false
    }
}
