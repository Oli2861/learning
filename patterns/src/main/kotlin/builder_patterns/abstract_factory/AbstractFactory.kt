package builder_patterns.abstract_factory

abstract class AbstractFactory {

    abstract fun createStorageSolution(): StorageSolution

    abstract fun createDatasource(): Datasource

}

class Factory1 : AbstractFactory() {

    override fun createStorageSolution(): StorageSolution {
        return Storage1()
    }

    override fun createDatasource(): Datasource {
        return Datasource2()
    }

}

class Factory2 : AbstractFactory() {

    override fun createStorageSolution(): StorageSolution {
        return Storage2()
    }

    override fun createDatasource(): Datasource {
        return Datasource1()
    }

}