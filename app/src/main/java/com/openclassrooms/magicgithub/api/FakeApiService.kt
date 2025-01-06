// FakeApiService.kt
package com.openclassrooms.magicgithub.api

import com.openclassrooms.magicgithub.model.User

class FakeApiService {

    private val users = mutableListOf<User>()

    init {
        users.addAll(FakeApiServiceGenerator.FAKE_USERS)
    }

    fun getUsers(): List<User> {
        return users
    }

    fun addRandomUser() {
        val randomUser = FakeApiServiceGenerator.generateRandomUser()
        users.add(randomUser)
    }

    fun deleteUser(user: User) {
        users.remove(user)
    }
}