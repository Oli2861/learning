package model;

import decisionstrategy.DesireDecisionStrategy;
import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistNormal;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.statistic.Count;
import desmoj.core.statistic.Tally;
import entities.Cashier;
import entities.Customer;
import entities.Server;
import events.CustomerGeneratorEvent;
import misc.QueueWrapper;
import java.util.concurrent.TimeUnit;

public abstract class CafeteriaModel extends Model {

    /**
     * Random number stream for the arrival time of the customers.
     * Initialized in the init() method.
     */
    private ContDistExponential customerArrivalTime;

    // Self serving bar
    /**
     * Queue of servers of the self-serving bar. Does not represent actual workers but free slots where customers can take what they desired snack.
     */
    public Queue<Server> selfServingBarSlots;

    /**
     * Customer queue in front of the self-serving bar.
     */
    public Queue<Customer> customerQueueSelfService;

    /**
     * Random number stream for the time required to grab a snack. Therefore, describes how long a customer occupies the self serving bar.
     */
    private ContDistUniform snackGrabbingTime;

    //  Counter
    /**
     * Random number stream for the food service time.
     * Describes how long a server at the counter needs to serve meal 1.
     */
    private ContDistExponential foodServingTimeMenu1;

    /**
     * Random number stream for the food service time.
     * Describes how long a server at the counter needs to serve meal 2.
     */
    private ContDistNormal foodServingTimeMenu2;

    /**
     * Queue of customers waiting in front of the counter 1.
     * Customers will be removed from this queue by the CounterServer at counter 1.
     */
    public Queue<Customer> customersQueueCounter1;

    /**
     * Queue of customers waiting in front of the counter 2.
     * Customers will be removed from this queue by the CounterServer at counter 2.
     */
    public Queue<Customer> customersQueueCounter2;

    /**
     * Queue of idling servers at the counter 1.
     * If there is no customer to serve, the server will wait in the queue until the next customer arrives.
     */
    public Queue<Server> serverQueueCounter1;
    /**
     * Queue of idling servers at the counter 2.
     * If there is no customer to serve, the server will wait in the queue until the next customer arrives.
     */
    public Queue<Server> serverQueueCounter2;

    // Checkout
    /**
     * Random number stream for the checkout time paying in cash.
     * Describes how long a checkout will take when the customer pays in cash.
     */
    private ContDistUniform checkoutTimeCash;

    /**
     * Random number stream for the checkout time paying with card.
     * Describes how long a checkout will take when the customer pays with card.
     */
    private ContDistUniform checkoutTimeCard;

    /**
     * Queue of customers waiting to pay.
     */
    public Queue<Customer> customerQueuePaymentShared;

    /**
     * Queue of cashiers idling at checkout counter.
     * Cashier will wait in this queue until a customer arrives.
     */
    public Queue<Cashier> cashierQueue;

    // Stats
    public Count numberOfCustomers;
    public Count numberOfServedCustomers;
    public Count numberOfCustomersLeft;
    public Count totalWaitingTimeOfAllCustomers;
    public Tally customerWaitingTimeServing;
    public Tally customerWaitingTimePayment;
    public Tally customerWaitingTimeTotal;
    public Tally customerSystemTime;
    /**
     * Time after which no new customers are allowed to enter the system, existing customers will be processed further.
     */
    public TimeInstant closingTime;

    public CafeteriaModel() {
        super(null, CafeteriaModel.class.getName(), true, true);
        closingTime = new TimeInstant(getCLOSING_TIME_IN_MINUTES(), TimeUnit.MINUTES);
    }

    /**
     * Places events on simulator list to start the simulation.
     */
    @Override
    public void doInitialSchedules() {
        CustomerGeneratorEvent customerGeneratorEvent = new CustomerGeneratorEvent(this);
        customerGeneratorEvent.schedule(new TimeSpan(0, TimeUnit.MINUTES));
    }

    /**
     * Returns description of the model used in the report.
     *
     * @return description of the model.
     */
    @Override
    public String description() {
        return "Cafeteria model";
    }

    /**
     * Initialize static components (Queues. Distributions).
     */
    @Override
    public void init() {
        initDistributions();
        initQueues();
        initStats();
    }

    private void initDistributions() {
        customerArrivalTime = new ContDistExponential(this, "CustomerArrivalTime", getMEAN_TIME_BETWEEN_ARRIVALS(), true, true);
        foodServingTimeMenu1 = new ContDistExponential(this, "Menu1ServingTime", getMEAN_MENU_1(), true, true);
        foodServingTimeMenu2 = new ContDistNormal(this, "Menu2ServingTime", getMEAN_MENU_2(), getSTANDARD_DEVIATION(), true, true);
        snackGrabbingTime = new ContDistUniform(this, "SnackGrabbingTimeStream", getMIN_SNACK_GRABBING_TIME(), getMAX_SNACK_GRABBING_TIME(), true, true);
        checkoutTimeCash = new ContDistUniform(this, "CheckoutCashTimeStream", getMIN_CHECKOUT_TIME_CASH(), getMAX_CHECKOUT_TIME_CASH(), true, true);
        checkoutTimeCard = new ContDistUniform(this, "CheckoutCardTimeStream", getMIN_CHECKOUT_TIME_CARD(), getMAX_CHECKOUT_TIME_CARD(), true, true);
    }

    private void initQueues() {
        // Counter 1
        customersQueueCounter1 = new QueueWrapper<>(this, "Customer queue in front of the food serving counter 1", true, true);
        serverQueueCounter1 = new QueueWrapper<>(this, "Queue of servers idling at the food serving counter1", true, true);
        for (int i = 0; i < getNUMBER_OF_SERVERS_COUNTER_1(); i++) {
            serverQueueCounter1.insert(new Server(this));
        }
        // Counter 2
        customersQueueCounter2 = new QueueWrapper<>(this, "Customer queue in front of the food serving counter 2", true, true);
        serverQueueCounter2 = new QueueWrapper<>(this, "Queue of servers idling at the food serving counter 2", true, true);
        for (int i = 0; i < getNUMBER_OF_SERVERS_COUNTER_2(); i++) {
            serverQueueCounter2.insert(new Server(this));
        }
        // Self serving bar
        customerQueueSelfService = new QueueWrapper<>(this, "Customer queue in front of the self serving bar.", true, true);
        selfServingBarSlots = new QueueWrapper<>(this, "Queue of servers of the self-serving bar. Does not represent workers but rather available slots.", true, true);
        for (int i = 0; i < getCAPACITY_SELF_SERVICE_BAR(); i++) {
            selfServingBarSlots.insert(new Server(this));
        }
        // Payment
        customerQueuePaymentShared = new QueueWrapper<>(this, "Customer queue in front of the payment area", true, true);
        cashierQueue = new QueueWrapper<>(this, "Queue of idling cashiers at checkout area", true, true);
        for (int i = 0; i < getNUMBER_OF_CASHIERS_CHECKOUT_COUNTER(); i++) {
            cashierQueue.insert(new Cashier(this));
        }
    }

    private void initStats() {
        numberOfCustomers = new Count(this, "Number of customers", true, false);
        numberOfServedCustomers = new Count(this, "Number of customers that have been served but did not pay yet", true, false);
        numberOfCustomersLeft = new Count(this, "Number of customers that left the system", true, false);
        totalWaitingTimeOfAllCustomers = new Count(this, "Total waiting time of all customers --> total total waiting time :)", true, false);

        customerSystemTime = new Tally(this, "Time customers spend in the system.", true, false);
        customerWaitingTimeServing = new Tally(this, "Time customers wait for serving", true, false);
        customerWaitingTimePayment = new Tally(this, "Time customers wait for payment", true, false);
        customerWaitingTimeTotal = new Tally(this, "Time customers wait in total", true, false);
    }

    public boolean isStillOpen() {
        return TimeInstant.isBefore(this.presentTime(), closingTime);
    }

    public boolean isStillOpen(int amountOfGeneratedCustomers) {
        if (getMAX_AMOUNT_OF_CUSTOMERS() == null) {
            return isStillOpen();
        } else {
            return isStillOpen() && amountOfGeneratedCustomers < getMAX_AMOUNT_OF_CUSTOMERS();
        }
    }

    public double getCustomerArrivalTime() {
        return customerArrivalTime.sample();
    }
    public double getServingTimeMenu1() {
        return foodServingTimeMenu1.sample();
    }
    public double getServingTimeMenu2() {
        return foodServingTimeMenu2.sample();
    }
    public double getCheckoutTimeCash() {
        return checkoutTimeCash.sample();
    }
    public double getCheckoutTimeCard() {
        return checkoutTimeCard.sample();
    }
    public double getSnackGrabbingTime() {
        return snackGrabbingTime.sample();
    }
    public abstract int getCARD_PAYMENT_PROBABILTY();
    public abstract DesireDecisionStrategy getActiveDecisionStrategy();
    abstract int getCLOSING_TIME_IN_MINUTES();
    abstract int getCAPACITY_SELF_SERVICE_BAR();
    abstract int getNUMBER_OF_SERVERS_COUNTER_1();
    abstract int getNUMBER_OF_SERVERS_COUNTER_2();
    abstract int getNUMBER_OF_CASHIERS_CHECKOUT_COUNTER();
    abstract Integer getMAX_AMOUNT_OF_CUSTOMERS();
    abstract double getMEAN_TIME_BETWEEN_ARRIVALS();
    abstract double getMEAN_MENU_1();
    abstract double getMEAN_MENU_2();
    abstract double getSTANDARD_DEVIATION();
    abstract int getMIN_SNACK_GRABBING_TIME();
    abstract int getMAX_SNACK_GRABBING_TIME();
    abstract int getMIN_CHECKOUT_TIME_CASH();
    abstract int getMAX_CHECKOUT_TIME_CASH();
    abstract int getMIN_CHECKOUT_TIME_CARD();
    abstract int getMAX_CHECKOUT_TIME_CARD();

    @Override
    public String toString() {
        return "CafeteriaModel{" +
                ",\n activeDecisionStrategy=" + getActiveDecisionStrategy() +
                ",\n} " + super.toString();
    }

    public boolean areQueuesEmpty() {
        return customersQueueCounter1.isEmpty() && customersQueueCounter2.isEmpty() && customerQueueSelfService.isEmpty() && customerQueuePaymentShared.isEmpty();
    }
}