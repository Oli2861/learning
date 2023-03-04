package entities;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class Server extends Entity {
    public Server(Model model) {
        super(model, Server.class.getName(), true);
    }

}
