package misc;

import desmoj.core.simulator.ModelCondition;
import desmoj.core.simulator.TimeInstant;
import model.CafeteriaModel;

public class StopCondition extends ModelCondition {
    private final TimeInstant stopTime;
    private final CafeteriaModel model;

    public StopCondition(CafeteriaModel model, TimeInstant stopTime) {
        super(model, StopCondition.class.getName(), true);
        this.model = model;
        this.stopTime = stopTime;
    }

    @Override
    public boolean check() {
        boolean queuesEmpty = model.areQueuesEmpty();
        boolean timeAfterStopTime = TimeInstant.isAfterOrEqual(model.presentTime(), stopTime);
        return queuesEmpty && timeAfterStopTime;
    }
}
