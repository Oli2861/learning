package events;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeSpan;
import entities.Customer;
import entities.Server;
import model.CafeteriaModel;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Arrival of a customer at the monitor.
 */
public class CustomerArrival extends Event<Customer> {
    private final CafeteriaModel model;

    public CustomerArrival(CafeteriaModel model) {
        super(model, CustomerArrival.class.getName(), true);
        this.model = model;
    }

    /**
     * Describes what happens on customer arrival.
     * 1. Customer checks monitor and decides what he wants.
     * 2. Based on that the customer will enter the queue for
     * - Counter 1
     * - Counter 2
     * - Self service area
     *
     * @param customer customer entity that just arrived.
     */
    @Override
    public void eventRoutine(Customer customer) {
        // Customer decides at the monitor what he wants
        switch (customer.generateDesire()) {
            case MENU1 -> processMealChoice(customer, model.customersQueueCounter1, model.serverQueueCounter1, model::getServingTimeMenu1);
            case MENU2 -> processMealChoice(customer, model.customersQueueCounter2, model.serverQueueCounter2, model::getServingTimeMenu2);
            case SNACK -> processMealChoice(customer, model.customerQueueSelfService, model.selfServingBarSlots, model::getSnackGrabbingTime);
        }

        model.numberOfCustomers.update();
    }

    /**
     * Process when a customer chose a meal available at either counter 1 or counter 2.
     *
     * @param customer      Customer entity.
     * @param customerQueue The queue of either counter 1 or counter 2.
     * @param serverQueue   The queue of servers either of counter 1 or counter 2.
     */
    private void processMealChoice(Customer customer, Queue<Customer> customerQueue, Queue<Server> serverQueue, Supplier<Double> foodServingTime) {
        customer.setStartWaitingForServing();
        customerQueue.insert(customer);

        if (serverQueue.isEmpty()) {
            // Customer was sent to the queue as there is no server available.
        } else {
            Server server = serverQueue.removeFirst();
            customerQueue.remove(customer);
            customer.setEndWaitingForServing();
            // Schedule end of serving for the customer based on current time and the time it takes to serve him.
            CustomerServingEnd customerServingEnd = new CustomerServingEnd(model, customerQueue, serverQueue, foodServingTime);
            customerServingEnd.schedule(server, customer, new TimeSpan(foodServingTime.get(), TimeUnit.SECONDS));
        }
    }

}
