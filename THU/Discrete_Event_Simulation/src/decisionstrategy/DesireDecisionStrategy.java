package decisionstrategy;

import model.CafeteriaModel;

public interface DesireDecisionStrategy {
    /**
     * Determines the desire of the customer. Potentially based on some system parameters such as queue length.
     * @param model reference to the model.
     * @return the desire of the customer (e.g. meal 1).
     */
    CustomerDesire getDecision(CafeteriaModel model);
}