package events;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.TimeSpan;
import entities.Cashier;
import entities.Customer;
import misc.PaymentMethod;
import model.CafeteriaModel;

import java.util.concurrent.TimeUnit;

/**
 * Represents the end of the checkout process and therefore also symbolizes the end of the customer entity in this system.
 */
public class CustomerPaymentEnd extends EventOf2Entities<Cashier, Customer> {
    private CafeteriaModel model;

    public CustomerPaymentEnd(CafeteriaModel model) {
        super(model, CustomerPaymentEnd.class.getName(), true);
        this.model = model;
    }

    /**
     * Describes the end of the payment process.
     * @param cashier Cashiers that processed the payment.
     * @param customer Customer that was processed.
     */
    @Override
    public void eventRoutine(Cashier cashier, Customer customer) {
        sendTraceNote("Customer has paid and therefore left the system. Customer: " + customer.toString());

        // Proceed with cashier:
        if (model.customerQueuePaymentShared.isEmpty()) {
            model.cashierQueue.insert(cashier);
        } else {
            Customer next = model.customerQueuePaymentShared.removeFirst();

            next.setEndWaitingForPayment();
            createAndScheduleCustomerPaymentEnd(model, cashier, next);
        }
    }

    /**
     * Creates and schedules a CustomerPaymentEnd event based. The time of the event is based on the checkout time, that is in turn based on the payment method of the customer.
     *
     * @param model    reference of the cafeteria model.
     * @param cashier  reference of the cashier.
     * @param customer reference of the paying customer.
     */
    public static void createAndScheduleCustomerPaymentEnd(CafeteriaModel model, Cashier cashier, Customer customer) {
        updateStats(model, customer);
        CustomerPaymentEnd event = new CustomerPaymentEnd(model);
        // Determine checkout time based on payment method
        double checkoutTime;
        if (customer.generatePaymentMethod() == PaymentMethod.CARD) {
            checkoutTime = model.getCheckoutTimeCard();
        } else {
            checkoutTime = model.getCheckoutTimeCash();
        }
        event.schedule(cashier, customer, new TimeSpan(checkoutTime, TimeUnit.SECONDS));
    }

    private static void updateStats(CafeteriaModel model, Customer customer){
        model.numberOfCustomersLeft.update();
        model.customerSystemTime.update();
        model.customerSystemTime.update(customer.leaveSystem());
        model.totalWaitingTimeOfAllCustomers.update((long) customer.getTotalWaitingTime());

        model.customerWaitingTimePayment.update(customer.getWaitingTimeForPayment());
        model.customerWaitingTimeServing.update(customer.getWaitingTimeForServing());
        model.customerWaitingTimeTotal.update(customer.getTotalWaitingTime());
    }


}
