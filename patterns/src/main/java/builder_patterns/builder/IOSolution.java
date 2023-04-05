package builder_patterns.builder;

import builder_patterns.abstract_factory.Datasource;
import builder_patterns.abstract_factory.StorageSolution;

class IOSolution{

    private Datasource datasource;
    private StorageSolution storageSolution;

    public IOSolution(Datasource datasource, StorageSolution storageSolution){
        this.datasource = datasource;
        this.storageSolution = storageSolution;
    }

    public Datasource datasource(){
        return datasource;
    }

    public StorageSolution storageSolution(){
        return storageSolution;
    }

}