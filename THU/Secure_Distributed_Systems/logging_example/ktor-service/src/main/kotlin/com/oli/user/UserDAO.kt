package com.oli.user

interface UserDAO {
    suspend fun create(user: User): User?
    suspend fun readAll(): List<User>
    suspend fun read(id: Int): User?
    suspend fun update(user: User): Boolean
    suspend fun delete(id: Int): Boolean
}