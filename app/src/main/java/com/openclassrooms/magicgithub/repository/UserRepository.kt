// UserRepository.kt
package com.openclassrooms.magicgithub.repository

import com.openclassrooms.magicgithub.api.FakeApiService
import com.openclassrooms.magicgithub.model.User

class UserRepository(private val apiService: FakeApiService) {

    fun getUsers(): List<User> {
        return apiService.getUsers()
    }

    fun addRandomUser() {
        apiService.addRandomUser()
    }

    fun deleteUser(user: User) {
        apiService.deleteUser(user)
    }
}