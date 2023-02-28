package com.oli.proxies

interface AccountingServiceProxy{
    fun authorize(sagaId: Int, userId: Int, paymentInfo: String): Boolean
}
class AccountingServiceProxyImpl: AccountingServiceProxy{
    override fun authorize(sagaId: Int, userId: Int, paymentInfo: String): Boolean {
        return false
    }
}