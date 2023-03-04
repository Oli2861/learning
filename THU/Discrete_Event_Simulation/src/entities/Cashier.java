package entities;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class Cashier extends Entity {
    public Cashier(Model model){
        super(model, Cashier.class.getName(), true);
    }
}
