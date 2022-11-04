package builder_patterns.multiton;

import java.util.HashMap;

public class Multiton {
    private static HashMap<String, Multiton> INSTANCES = new HashMap<>();
    private String resource;

    private Multiton() {
        resource = "resource";
    }

    public synchronized Multiton getInstance(String key){
        Multiton instance = INSTANCES.get(key);
        if(instance == null){
            instance = new Multiton();
            INSTANCES.put(key, instance);
        }
        return INSTANCES.get(key);
    }

}