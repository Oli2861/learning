package com.oli.order

import com.oli.persistence.OrderDAOImpl
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.plugin.koin

fun Application.orderModule() {
    koin {
        modules(orderKoinModule)
        configureOrderRouting()
    }
}

fun Application.configureOrderRouting(){
    routing {
        orderRouting()
    }
}

private val orderKoinModule = module {

    single<OrderDAO> {
        OrderDAOImpl()
    }

    single<OrderService> {
        OrderService(orderDAO = get(), logger = get())
    }

}