package model;

import decisionstrategy.DesireDecisionStrategy;
import decisionstrategy.SimpleDesireDecisionStrategy;

public class NominalCafeteriaModel extends CafeteriaModel {
    protected final int CAPACITY_SELF_SERVICE_BAR = 1;
    protected final int NUMBER_OF_SERVERS_COUNTER_1 = 1;
    protected final int NUMBER_OF_SERVERS_COUNTER_2 = 1;
    protected final int NUMBER_OF_CASHIERS_CHECKOUT_COUNTER = 1;
    protected final int CARD_PAYMENT_PROBABILTY = 85;
    protected final int CLOSING_TIME_IN_MINUTES = 60;
    protected final Integer MAX_AMOUNT_OF_CUSTOMERS = null;
    protected final DesireDecisionStrategy activeDecisionStrategy = new SimpleDesireDecisionStrategy();
    protected double MEAN_TIME_BETWEEN_ARRIVALS = 6.6667;
    protected final double MEAN_MENU_1 = 5.67596;
    protected final double MEAN_MENU_2 = 9.95262;
    protected final double STANDARD_DEVIATION = 1.86631;
    protected final int MIN_SNACK_GRABBING_TIME = 3;
    protected final int MAX_SNACK_GRABBING_TIME = 20;
    protected final int MIN_CHECKOUT_TIME_CASH = 10;
    protected final int MAX_CHECKOUT_TIME_CASH = 60;
    protected final int MIN_CHECKOUT_TIME_CARD = 3;
    protected final int MAX_CHECKOUT_TIME_CARD = 15;

    public NominalCafeteriaModel(){
        super();
    }

    @Override
    public int getCAPACITY_SELF_SERVICE_BAR() {
        return CAPACITY_SELF_SERVICE_BAR;
    }

    @Override
    public int getNUMBER_OF_SERVERS_COUNTER_1() {
        return NUMBER_OF_SERVERS_COUNTER_1;
    }

    @Override
    public int getNUMBER_OF_SERVERS_COUNTER_2() {
        return NUMBER_OF_SERVERS_COUNTER_2;
    }

    @Override
    public int getNUMBER_OF_CASHIERS_CHECKOUT_COUNTER() {
        return NUMBER_OF_CASHIERS_CHECKOUT_COUNTER;
    }

    @Override
    public int getCARD_PAYMENT_PROBABILTY() {
        return CARD_PAYMENT_PROBABILTY;
    }

    @Override
    public int getCLOSING_TIME_IN_MINUTES() {
        return CLOSING_TIME_IN_MINUTES;
    }

    @Override
    public Integer getMAX_AMOUNT_OF_CUSTOMERS() {
        return MAX_AMOUNT_OF_CUSTOMERS;
    }

    @Override
    public DesireDecisionStrategy getActiveDecisionStrategy() {
        return activeDecisionStrategy;
    }

    @Override
    public double getMEAN_TIME_BETWEEN_ARRIVALS() {
        return MEAN_TIME_BETWEEN_ARRIVALS;
    }

    @Override
    public double getMEAN_MENU_1() {
        return MEAN_MENU_1;
    }

    @Override
    public double getMEAN_MENU_2() {
        return MEAN_MENU_2;
    }

    @Override
    public double getSTANDARD_DEVIATION() {
        return STANDARD_DEVIATION;
    }

    @Override
    public int getMIN_SNACK_GRABBING_TIME() {
        return MIN_SNACK_GRABBING_TIME;
    }

    @Override
    public int getMAX_SNACK_GRABBING_TIME() {
        return MAX_SNACK_GRABBING_TIME;
    }

    @Override
    public int getMIN_CHECKOUT_TIME_CASH() {
        return MIN_CHECKOUT_TIME_CASH;
    }

    @Override
    public int getMAX_CHECKOUT_TIME_CASH() {
        return MAX_CHECKOUT_TIME_CASH;
    }

    @Override
    public int getMIN_CHECKOUT_TIME_CARD() {
        return MIN_CHECKOUT_TIME_CARD;
    }

    @Override
    public int getMAX_CHECKOUT_TIME_CARD() {
        return MAX_CHECKOUT_TIME_CARD;
    }

    public void setMEAN_TIME_BETWEEN_ARRIVALS(double meanTimeBetweenArrivals){
        MEAN_TIME_BETWEEN_ARRIVALS = meanTimeBetweenArrivals;
    }

    @Override
    public String toString() {
        return "NominalCafeteriaModel{" +
                "CAPACITY_SELF_SERVICE_BAR=" + CAPACITY_SELF_SERVICE_BAR +
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
                "} " + super.toString();
    }
}
