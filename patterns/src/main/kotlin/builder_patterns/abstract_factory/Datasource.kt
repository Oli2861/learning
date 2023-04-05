package builder_patterns.abstract_factory

interface Datasource {
    fun retrieveData(): String
}

class TwitterAPI : Datasource {
    override fun retrieveData(): String {
        return "Data from datasource1"
    }
}

class BeautifulSoupWebScraper : Datasource {
    override fun retrieveData(): String {
        return "Data from datasource2"
    }
}