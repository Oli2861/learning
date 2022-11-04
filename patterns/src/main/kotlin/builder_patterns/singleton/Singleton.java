package builder_patterns.singleton;

public class Singleton {

    private volatile String data;
    private static Singleton INSTANCE;

    private Singleton() {
    }

    public synchronized Singleton getInstance() {
        if (INSTANCE == null) {
            return new Singleton();
        } else {
            return INSTANCE;
        }
    }

    public synchronized void init(String data) throws Exception {
        if (data != null) {
            this.data = data;
        } else {
            throw new Exception("Singleton.Singleton is already initialized.");
        }
    }

}
