package com.oli.proxies
interface ConsumerServiceProxy{
    fun verifyConsumerDetails(sagaId: Int): Boolean
}
class ConsumerServiceProxyImpl: ConsumerServiceProxy{
    override fun verifyConsumerDetails(sagaId: Int): Boolean {
        return false
    }
}