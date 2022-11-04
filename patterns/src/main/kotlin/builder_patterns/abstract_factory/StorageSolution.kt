package builder_patterns.abstract_factory

interface StorageSolution {
    fun saveData(data: String)
}

class Storage1 : StorageSolution {
    override fun saveData(data: String) {
        println(data)
    }
}

class Storage2 : StorageSolution {
    override fun saveData(data: String) {
        println(data)
    }
}