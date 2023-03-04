package decisionstrategy;

import model.CafeteriaModel;

import java.util.Random;

public class MyDesireDecisionStrategy implements DesireDecisionStrategy {
    private static final Random random = new Random();
    /**
     * Simple decision strategy to resort to if queue lengths are small.
     */
    private static final SimpleDesireDecisionStrategy simpleDecisionStrategy = new SimpleDesireDecisionStrategy();

    /**
     * Determines which queue a customer will enter based on the probabilities from the descriptions and the queue lengths.
     * If there is no queue longer than 4 customers, the customer wont bother with queue lengths and resort to the simple decision strategy.
     * @param model reference of the cafeteria mode to get the queue lengths.
     * @return The decision of the customer.
     */
    @Override
    public CustomerDesire getDecision(CafeteriaModel model) {
        int menu1QueueLength = model.customersQueueCounter1.size();
        int menu2QueueLength = model.customersQueueCounter2.size();
        int selfServiceQueueLength = model.customerQueueSelfService.size();

        int totalQueueLength = menu1QueueLength + menu2QueueLength + selfServiceQueueLength;
        int maxQueueLength = Math.max(Math.max(menu2QueueLength, menu1QueueLength), selfServiceQueueLength);

        // Customers resort to the simple decision strategy if there are no queues.
        if(maxQueueLength < 3){
            return simpleDecisionStrategy.getDecision(model);
        }

        // Determine percentages of the total queue shares
        float percentageMenu1 = (float) menu1QueueLength / totalQueueLength;
        float percentageMenu2 = (float) menu2QueueLength / totalQueueLength;

        int bonus = 10;
        int rawProbabilityMenu1 = 60;
        int rawProbabilityMenu2 = 25;
        int randomNumber = random.nextInt(100 + bonus);
        //System.out.println(menu1QueueLength);
        // Calculate bonuses based on the total queue length for each queue
        float bonusMenu1 = (1 - percentageMenu1) * bonus;
        float bonusMenu2 = (1 - percentageMenu2) * bonus;

        // Determine thresholds for to determine when a customer chooses a certain queue.
        float thresholdMenu1 = rawProbabilityMenu1 + bonusMenu1;
        float thresholdMenu2 = thresholdMenu1 + rawProbabilityMenu2 + bonusMenu2;
        //System.out.println(rawProbabilityMenu1 + " + " + bonusMenu1 + " = " + thresholdMenu1);
        if (randomNumber < thresholdMenu1) {
            return CustomerDesire.MENU1;
        } else if (randomNumber < thresholdMenu2) {
            return CustomerDesire.MENU2;
        } else {
            return CustomerDesire.SNACK;
        }
    }

}