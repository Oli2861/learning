package builder_patterns.abstract_factory

abstract class AbstractFactory {

    abstract fun createStorageSolution(): StorageSolution

    abstract fun createDatasource(): Datasource

}

class Factory1 : AbstractFactory() {

    override fun createStorageSolution(): StorageSolution {
        return MySqlDatabase()
    }

    override fun createDatasource(): Datasource {
        return BeautifulSoupWebScraper()
    }

}

class Factory2 : AbstractFactory() {

    override fun createStorageSolution(): StorageSolution {
        return MongoDB()
    }

    override fun createDatasource(): Datasource {
        return TwitterAPI()
    }

}