package events;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.TimeSpan;
import entities.Customer;
import model.CafeteriaModel;

import java.util.concurrent.TimeUnit;

public class CustomerGeneratorEvent extends ExternalEvent {
    private final CafeteriaModel model;
    public static int amountOfGeneratedCustomers = 0;

    public CustomerGeneratorEvent(CafeteriaModel model) {
        super(model, CustomerGeneratorEvent.class.getName(), true);
        this.model = model;
    }

    @Override
    public void eventRoutine() {
        if (model.isStillOpen(amountOfGeneratedCustomers)) {
            // Create customer entity
            Customer customer = new Customer(model);
            amountOfGeneratedCustomers++;

            // Create customer arrival event and schedule it for current point in time.
            CustomerArrival customerArrivalEvent = new CustomerArrival(model);
            customerArrivalEvent.schedule(customer, new TimeSpan(0, TimeUnit.SECONDS));

            // Schedule self for next arrival (based on current point in time)
            double nextArrival = model.getCustomerArrivalTime();
            schedule(new TimeSpan(nextArrival, TimeUnit.SECONDS));
        }
    }
}
