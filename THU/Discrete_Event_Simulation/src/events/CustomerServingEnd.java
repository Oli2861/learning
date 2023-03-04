package events;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import entities.Cashier;
import entities.Server;
import entities.Customer;
import model.CafeteriaModel;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Represents the end of the food serving process.
 */
public class CustomerServingEnd extends EventOf2Entities<Server, Customer> {
    private final Supplier<Double> foodServingTimeSupplier;
    private CafeteriaModel model;
    private Queue<Customer> associatedCustomerQueue;
    private Queue<Server> associatedServerQueue;

    /**
     * Event indicating that the customer has received his meal.
     *
     * @param model                   cafeteria model.
     * @param associatedCustomerQueue the customer queue to draw further customers from.
     * @param associatedServerQueue   the server queue to insert the server into if all customers are served.
     * @param foodServingTimeSupplier supplier to draw food serving time from.
     */
    public CustomerServingEnd(CafeteriaModel model, Queue<Customer> associatedCustomerQueue, Queue<Server> associatedServerQueue, Supplier<Double> foodServingTimeSupplier) {
        super(model, CustomerServingEnd.class.getName(), true);
        this.model = model;
        this.associatedCustomerQueue = associatedCustomerQueue;
        this.associatedServerQueue = associatedServerQueue;
        this.foodServingTimeSupplier = foodServingTimeSupplier;
    }

    /**
     * Describes the end of the food serving process.
     *
     * @param counterServer Server that processed the customers request (food wish).
     * @param customer      Customer that got processed.
     */
    @Override
    public void eventRoutine(Server counterServer, Customer customer) {
        sendTraceNote("Customer has been served: " + customer.toString() + " by server " + counterServer.toString());
        scheduleCustomerForPayment(customer);
        proceedWithServer(counterServer);
        model.numberOfServedCustomers.update();
    }

    /**
     * Schedule the customer for payment.
     *
     * @param customer customer reference.
     */
    private void scheduleCustomerForPayment(Customer customer) {
        model.customerQueuePaymentShared.insert(customer);
        customer.setStartWaitingForPayment();

        if (model.cashierQueue.isEmpty()) {
            // Customer was sent into the queue as there is no cashier available.
        } else {
            Cashier cashier = model.cashierQueue.removeFirst();
            model.customerQueuePaymentShared.remove(customer);
            customer.setEndWaitingForPayment();

            CustomerPaymentEnd.createAndScheduleCustomerPaymentEnd(model, cashier, customer);
        }
    }

    /**
     * Proceed with server: Either put back into queue if there are no customers waiting or keep serving customers from the queue.
     *
     * @param server server reference.
     */
    private void proceedWithServer(Server server) {
        if (associatedCustomerQueue.isEmpty()) {
            // No customers waiting --> go into idle queue
            associatedServerQueue.insert(server);
        } else {
            // Customer waiting --> schedule the customer serving end event for that waiting customer based on the current time and the serving time.
            Customer next = associatedCustomerQueue.removeFirst();
            next.setEndWaitingForServing();

            CustomerServingEnd customerServingEndEvent = new CustomerServingEnd(model, associatedCustomerQueue, associatedServerQueue, foodServingTimeSupplier);
            customerServingEndEvent.schedule(server, next, new TimeSpan(foodServingTimeSupplier.get(), TimeUnit.SECONDS));
        }
    }
}
