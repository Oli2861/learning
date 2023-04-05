package builder_patterns.prototype;

/**
 * Concrete prototype, inherits from abstract prototype and implements the cloneable interface
 */
public class DataFile extends File implements Cloneable{
    private String name;
    private int[][] X;

    public DataFile(String name, int[][] X) {
        super(name);
        this.X = X;
        this.name = name;
    }

    public int[][] getX() {
        return X;
    }
    public String getName() {
        return name;
    }

    public DataFile clone() {
        return (DataFile) super.clone();
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof DataFile dataFile)) return false;
        return dataFile.name.equals(name) && dataFile.X == X;
    }
}

