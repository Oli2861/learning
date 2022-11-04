package builder_patterns.abstract_factory

interface Datasource {
    fun retrieveData(): String
}

class Datasource1 : Datasource {
    override fun retrieveData(): String {
        return "Data from datasource1"
    }
}

class Datasource2 : Datasource {
    override fun retrieveData(): String {
        return "Data from datasource2"
    }
}