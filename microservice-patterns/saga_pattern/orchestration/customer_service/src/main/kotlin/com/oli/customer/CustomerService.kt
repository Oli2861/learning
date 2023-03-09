package com.oli.customer

import com.oli.address.Address
import com.oli.address.AddressDAO
import com.oli.event.*
import org.slf4j.Logger

class CustomerService(
    private val customerDAO: CustomerDAO,
    private val addressDAO: AddressDAO,
    private val logger: Logger,
    private val messageBroker: MessageBroker
) {
    private val createOrderSagaReplyChannelName =
        System.getenv("CREATE_ORDER_SAGA_REPLY_CHANNEL") ?: "create_order_saga_reply_channel"

    /**
     * Create customer.
     * @param customer The customer to be created.
     * @return ID of the created customer
     */
    suspend fun create(customer: Customer): Int? {
        return customerDAO.create(customer)?.id
    }

    /**
     * Verify a customer by comparing the provided object with the one stored in the database.
     * @param customer The customer object to be compared with the stored one.
     * @return True if the provided object corresponds to the stored entity, false if not and null if there is no entity with the provided ID.
     */
    suspend fun verify(customer: Customer): Boolean? {
        val storedCustomer: Customer = read(customer.id) ?: return null
        val providedAddressKnown = customer.addresses.all { provided ->
            storedCustomer.addresses.any { stored -> stored.equalsIgnoreId(provided) }
        }
        return storedCustomer.equalIgnoreIdAndAddresses(customer) && providedAddressKnown
    }

    /**
     * Read customer.
     * @param id ID of the customer to be read.
     * @return Customer or null if not found.
     */
    suspend fun read(id: Int): Customer? {
        return customerDAO.read(id)
    }

    /**
     * Update a customer (addresses ignored).
     * @param id ID of the customer to be updated.
     * @param customer Customer to be updated.
     */
    suspend fun update(customer: CustomerNoAddress): Int {
        return customerDAO.update(customer)
    }

    /**
     * Update a address.
     * @param address The address to be updated.
     * @return The amount of affected entries.
     */
    suspend fun updateAddress(address: Address): Boolean {
        return addressDAO.update(address) == 1
    }

    /**
     * Delete a customer and all associated addresses.
     * @param id ID of the customer to be deleted.
     * @return The amount of affected entries.
     */
    suspend fun delete(id: Int): Int {
        return customerDAO.delete(id)
    }

    suspend fun handleEvent(correlationId: String, event: Event): String {
        logger.debug("Received event $event")
        return when (event) {
            is VerifyCustomerCommandEvent -> handleCustomerVerificationEvent(correlationId, event)
            else -> {
                logger.debug("Received unknown event type. Event: $event")
                "404"
            }
        }
    }

    private suspend fun handleCustomerVerificationEvent(correlationId: String, event: VerifyCustomerCommandEvent): String {
        val result = verify(event.customer)
        val response = VerifyCustomerReplyEvent(event, result)
        logger.debug("Customer verification for correlation id $correlationId completed.")
        return EventSerializer.serialize(response)
    }
}
