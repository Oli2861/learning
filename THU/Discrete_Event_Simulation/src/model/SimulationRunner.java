package model;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import misc.StopCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimulationRunner {

    public static void main(String[] args) {
        //runSimulation(new ImprovedModel());
        searchArrivalRate();
    }

    private static double runSimulation(CafeteriaModel model) {
        Experiment exp = new Experiment("CafeteriaExperiment");
        exp.setSeedGenerator(42);
        model.connectToExperiment(exp);

        exp.setShowProgressBar(false);
        TimeInstant stopTime = new TimeInstant(model.getCLOSING_TIME_IN_MINUTES(), TimeUnit.MINUTES);
        StopCondition condition = new StopCondition(model, stopTime);

        exp.stop(condition);
        exp.start();
        exp.report();
        exp.finish();

        return model.customerSystemTime.getMean();
    }

    private static double searchArrivalRate() {

        double meanTimeBetweenArrivals = 25;
        double lastOkayLoss = 1;
        double stepSize = 100;
        List<Double> meanTimeBetweenArrivalsHistory = new ArrayList<>();
        List<Double> lossHistory = new ArrayList<>();
        List<Double> stepSizeHistory = new ArrayList<>();

        while (lastOkayLoss > 0 && meanTimeBetweenArrivals > 0) {
            NominalCafeteriaModel model = new NoCashModel();
            model.setMEAN_TIME_BETWEEN_ARRIVALS(meanTimeBetweenArrivals);
            double loss = getLoss(300, 100, model);
            if (loss > 0) {
                meanTimeBetweenArrivalsHistory.add(meanTimeBetweenArrivals);
                lossHistory.add(lastOkayLoss);
                stepSizeHistory.add(stepSize / 100);
                lastOkayLoss = loss;
                stepSize = stepSize - Math.log(stepSize);
                meanTimeBetweenArrivals = meanTimeBetweenArrivals - (stepSize / 100);
            } else {
                break;
            }
        }

        double minimalMeanTimeBetweenArrivals = meanTimeBetweenArrivalsHistory.get(meanTimeBetweenArrivalsHistory.size() - 1);

        System.out.println("Mean time between arrivals history: " + meanTimeBetweenArrivalsHistory);
        System.out.println("Loss history: " + lossHistory);
        System.out.println("Step size history: " + stepSizeHistory);

        NominalCafeteriaModel model = new NoCashModel();
        model.setMEAN_TIME_BETWEEN_ARRIVALS(meanTimeBetweenArrivals);
        System.out.println("Mean time between arrivals: " + meanTimeBetweenArrivals);
        runSimulation(model);

        return minimalMeanTimeBetweenArrivals;
    }

    private static double getLoss(int allowedSystemTime, int numberOfSimulations, CafeteriaModel model) {
        List<Double> lossList = new ArrayList<>();

        for (int i = 0; i < numberOfSimulations; i++) {
            double meanCustomerSystemTime = runSimulation(model);
            double loss = allowedSystemTime - meanCustomerSystemTime;
            System.out.println(loss + " = " + allowedSystemTime + " - " + meanCustomerSystemTime);
            lossList.add(loss);
            if (loss <= 0) return loss;
        }

        return lossList.stream().mapToDouble(loss -> loss).average().getAsDouble();
    }

}
