package builder_patterns.prototype;

/**
 * Abstract prototype, implements Cloneable interface
 */
public abstract class File implements Cloneable {
    private String name;

    public File(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public File clone(){
        // Would handle special cases of all subclasses here.
        // Provides a shallow copy of the object.
        try {
            return (File) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
