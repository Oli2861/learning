package com.oli.proxies

interface KitchenServiceProxy {
    fun createTicket(sagaId: Int): Boolean
    fun cancelOrder(sagaId: Int): Int
    fun approveTicket(sagaId: Int): Int
}

class KitchenServiceProxyImpl : KitchenServiceProxy {
    override fun createTicket(sagaId: Int): Boolean {
        return false
    }

    override fun cancelOrder(sagaId: Int): Int {
        return -1
    }

    override fun approveTicket(sagaId: Int): Int {
        return -1
    }
}