package misc;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;

public class QueueWrapper<T extends Entity> extends Queue<T> {
    public QueueWrapper(Model model, String s, int i, int i1, boolean b, boolean b1) {
        super(model, s, i, i1, b, b1);
    }

    public QueueWrapper(Model model, String s, boolean b, boolean b1) {
        super(model, s, b, b1);
    }

    @Override
    public boolean insert(T t) {
        sendTraceNote("Added " + t + " to queue " + this + " of size " + this.size());
        return super.insert(t);
    }

    @Override
    public T removeFirst() {
        T t  = super.removeFirst();
        sendTraceNote("Removed " + t + " from queue " + this + " of size " + this.size());
        return t;
    }

    @Override
    public void remove(Entity entity) {
        sendTraceNote("Removed " + entity + " from queue " + this + " of size " + this.size());
        super.remove(entity);
    }
}
