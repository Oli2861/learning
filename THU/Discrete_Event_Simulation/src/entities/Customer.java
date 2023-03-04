package entities;

import decisionstrategy.CustomerDesire;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.TimeInstant;
import misc.PaymentMethod;
import model.CafeteriaModel;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Customer extends Entity {
    private CafeteriaModel model;
    private CustomerDesire desire;
    private PaymentMethod preferredPaymentMethod;
    private static final Random random = new Random();

    private TimeInstant systemEntryTime;
    private TimeInstant systemExitTime;
    private TimeInstant startWaitingForServing;
    private TimeInstant endWaitingForServing;
    private TimeInstant startWaitingForPayment;
    private TimeInstant endWaitingForPayment;

    public Customer(CafeteriaModel model) {
        super(model, Customer.class.getName(), true);
        this.model = model;
        this.systemEntryTime = model.presentTime();
    }

    public CustomerDesire generateDesire() {
        desire = model.getActiveDecisionStrategy().getDecision(model);
        return desire;
    }

    public PaymentMethod generatePaymentMethod() {
        int randomNumber = random.nextInt(100);

        if (randomNumber < model.getCARD_PAYMENT_PROBABILTY()) {
            preferredPaymentMethod = PaymentMethod.CARD;
        } else {
            preferredPaymentMethod = PaymentMethod.CASH;
        }

        return preferredPaymentMethod;
    }

    /**
     * Returns the time the customer spend in the system.
     *
     * @return system time of the customer.
     */
    public double leaveSystem() {
        systemExitTime = model.presentTime();
        return systemExitTime.getTimeAsDouble(TimeUnit.SECONDS) - systemEntryTime.getTimeAsDouble(TimeUnit.SECONDS);
    }

    public void setStartWaitingForServing() {
        this.startWaitingForServing = model.presentTime();
    }

    public void setEndWaitingForServing() {
        this.endWaitingForServing = model.presentTime();
    }

    public void setStartWaitingForPayment() {
        this.startWaitingForPayment = model.presentTime();
    }

    public void setEndWaitingForPayment() {
        this.endWaitingForPayment = model.presentTime();
    }

    public double getWaitingTimeForServing() {
        return endWaitingForServing.getTimeAsDouble(TimeUnit.SECONDS) - startWaitingForServing.getTimeAsDouble(TimeUnit.SECONDS);
    }

    public double getWaitingTimeForPayment() {
        return endWaitingForPayment.getTimeAsDouble(TimeUnit.SECONDS) - startWaitingForPayment.getTimeAsDouble(TimeUnit.SECONDS);
    }

    public double getTotalWaitingTime() {
        return getWaitingTimeForPayment() + getWaitingTimeForServing();
    }
}
