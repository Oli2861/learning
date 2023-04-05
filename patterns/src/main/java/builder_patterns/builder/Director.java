package builder_patterns.builder;

import builder_patterns.abstract_factory.TwitterAPI;
import builder_patterns.abstract_factory.MySqlDatabase;

public class Director {
    private IOBuilder builder;

    public Director(IOBuilder builder) {
        this.builder = builder;
    }

    public void construct() {
        // Defines order in which the build steps are called.
        builder.addDataSource(new TwitterAPI());
        builder.storageSolution(new MySqlDatabase());
    }

    public IOSolution getResult() {
        return builder.build();
    }

}
