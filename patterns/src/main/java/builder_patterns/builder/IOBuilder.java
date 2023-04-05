package builder_patterns.builder;

import builder_patterns.abstract_factory.Datasource;
import builder_patterns.abstract_factory.StorageSolution;

interface IOBuilder {
    public void addDataSource(Datasource datasource);
    public void storageSolution(StorageSolution storageSolution);
    public IOSolution build();
}

class Builder1 implements IOBuilder {
    private Datasource datasource;
    private StorageSolution storageSolution;

    @Override
    public void addDataSource(Datasource datasource) {
        this.datasource = datasource;
    }

    @Override
    public void storageSolution(StorageSolution storageSolution) {
        this.storageSolution = storageSolution;
    }

    public IOSolution build() {
        return new IOSolution(datasource, storageSolution);
    }
}
