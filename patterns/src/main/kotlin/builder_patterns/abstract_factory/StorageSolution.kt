package builder_patterns.abstract_factory

interface StorageSolution {
    fun saveData(data: String)
}

class MySqlDatabase : StorageSolution {
    override fun saveData(data: String) {
        println(data)
    }
}

class MongoDB : StorageSolution {
    override fun saveData(data: String) {
        println(data)
    }
}