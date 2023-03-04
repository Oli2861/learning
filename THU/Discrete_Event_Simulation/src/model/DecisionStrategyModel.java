package model;

import decisionstrategy.DesireDecisionStrategy;
import decisionstrategy.MyDesireDecisionStrategy;
import decisionstrategy.SimpleDesireDecisionStrategy;

public class DecisionStrategyModel extends BenchmarkingModel {
    private DesireDecisionStrategy activeDecisionStrategy = new MyDesireDecisionStrategy();

    public DecisionStrategyModel(){
        super();
        System.out.println(this.toString());
    }

    @Override
    public DesireDecisionStrategy getActiveDecisionStrategy() {
        return activeDecisionStrategy;
    }

    @Override
    public String toString() {
        return "DecisionStrategyModel{" +
                "NUMBER_OF_CASHIERS_CHECKOUT_COUNTER=" + NUMBER_OF_CASHIERS_CHECKOUT_COUNTER +
                ", activeDecisionStrategy=" + activeDecisionStrategy +
                ", MEAN_TIME_BETWEEN_ARRIVALS=" + MEAN_TIME_BETWEEN_ARRIVALS +
                ", CAPACITY_SELF_SERVICE_BAR=" + CAPACITY_SELF_SERVICE_BAR +
                ", NUMBER_OF_SERVERS_COUNTER_1=" + NUMBER_OF_SERVERS_COUNTER_1 +
                ", NUMBER_OF_SERVERS_COUNTER_2=" + NUMBER_OF_SERVERS_COUNTER_2 +
                ", NUMBER_OF_CASHIERS_CHECKOUT_COUNTER=" + NUMBER_OF_CASHIERS_CHECKOUT_COUNTER +
                ", CARD_PAYMENT_PROBABILTY=" + CARD_PAYMENT_PROBABILTY +
                ", CLOSING_TIME_IN_MINUTES=" + CLOSING_TIME_IN_MINUTES +
                ", MAX_AMOUNT_OF_CUSTOMERS=" + MAX_AMOUNT_OF_CUSTOMERS +
                ", activeDecisionStrategy=" + activeDecisionStrategy +
                ", MEAN_TIME_BETWEEN_ARRIVALS=" + MEAN_TIME_BETWEEN_ARRIVALS +
                ", MEAN_MENU_1=" + MEAN_MENU_1 +
                ", MEAN_MENU_2=" + MEAN_MENU_2 +
                ", STANDARD_DEVIATION=" + STANDARD_DEVIATION +
                ", MIN_SNACK_GRABBING_TIME=" + MIN_SNACK_GRABBING_TIME +
                ", MAX_SNACK_GRABBING_TIME=" + MAX_SNACK_GRABBING_TIME +
                ", MIN_CHECKOUT_TIME_CASH=" + MIN_CHECKOUT_TIME_CASH +
                ", MAX_CHECKOUT_TIME_CASH=" + MAX_CHECKOUT_TIME_CASH +
                ", MIN_CHECKOUT_TIME_CARD=" + MIN_CHECKOUT_TIME_CARD +
                ", MAX_CHECKOUT_TIME_CARD=" + MAX_CHECKOUT_TIME_CARD +
                ", selfServingBarSlots=" + selfServingBarSlots +
                ", customerQueueSelfService=" + customerQueueSelfService +
                ", customersQueueCounter1=" + customersQueueCounter1 +
                ", customersQueueCounter2=" + customersQueueCounter2 +
                ", serverQueueCounter1=" + serverQueueCounter1 +
                ", serverQueueCounter2=" + serverQueueCounter2 +
                ", customerQueuePaymentShared=" + customerQueuePaymentShared +
                ", cashierQueue=" + cashierQueue +
                ", numberOfCustomers=" + numberOfCustomers +
                ", numberOfServedCustomers=" + numberOfServedCustomers +
                ", numberOfCustomersLeft=" + numberOfCustomersLeft +
                ", totalWaitingTimeOfAllCustomers=" + totalWaitingTimeOfAllCustomers +
                ", customerWaitingTimeServing=" + customerWaitingTimeServing +
                ", customerWaitingTimePayment=" + customerWaitingTimePayment +
                ", customerWaitingTimeTotal=" + customerWaitingTimeTotal +
                ", customerSystemTime=" + customerSystemTime +
                ", closingTime=" + closingTime +
                "} " + super.toString();
    }
}
