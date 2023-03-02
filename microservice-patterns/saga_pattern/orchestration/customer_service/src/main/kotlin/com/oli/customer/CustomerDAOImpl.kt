package com.oli.customer

import com.oli.address.AddressEntity
import com.oli.address.Addresses
import com.oli.persistence.DatabaseFactory
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class CustomerDAOImpl : CustomerDAO {

    override suspend fun create(customer: Customer): Customer {
        val entity = DatabaseFactory.dbQuery {
            CustomerEntity.new {
                age = customer.age
                firstName = customer.firstName
                lastName = customer.lastName
            }
        }
        val addresses = DatabaseFactory.dbQuery {
            customer.addresses.map {
                AddressEntity.new {
                    customerId = entity.id
                    postCode = it.postCode
                    city = it.city
                    houseNumber = it.houseNumber
                }
            }
        }
        return Customer(entity, addresses)
    }

    override suspend fun read(id: Int): Customer? = DatabaseFactory.dbQuery {
        val entity: CustomerEntity = CustomerEntity.find(Customers.id eq id).firstOrNull() ?: return@dbQuery null
        val addresses = AddressEntity.find(Addresses.id eq entity.id.value).toList()
        return@dbQuery Customer(entity, addresses)
    }

    override suspend fun delete(id: Int): Int = DatabaseFactory.dbQuery {
        Addresses.deleteWhere { customerId eq id }
        Customers.deleteWhere { Customers.id eq id }
    }

    override suspend fun update(customer: CustomerNoAddress): Int = DatabaseFactory.dbQuery {
        Customers.update(where = { Customers.id eq customer.id }) {
            it[age] = customer.age
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
        }
    }
}
