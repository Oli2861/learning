package decisionstrategy;

import model.CafeteriaModel;

import java.util.Random;

public class SimpleDesireDecisionStrategy implements DesireDecisionStrategy {
    private static final Random random = new Random();

    /**
     * Determines the desire of the customer based on probabilities: 60% for menu 1, 25% for menu 2 and 15% for snack.
     * @param model reference to the model.
     * @return the desire of the customer (e.g. menu 1).
     */
    public CustomerDesire getDecision(CafeteriaModel model) {
        int randomNumber = random.nextInt(100);

        if (randomNumber < 60) {
            return CustomerDesire.MENU1;
        } else if (randomNumber < (60 + 25)) {
            return CustomerDesire.MENU2;
        } else {
            return CustomerDesire.SNACK;
        }
    }
}
