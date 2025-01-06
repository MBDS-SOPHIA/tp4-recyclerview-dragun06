package com.openclassrooms.magicgithub.api

import com.openclassrooms.magicgithub.model.User

class FakeApiService : ApiService {
    private val _users = FakeApiServiceGenerator.FAKE_USERS

    override fun getUsers(): List<User> {
        return _users
    }

    override fun addRandomUser() {
        val randomUser = FakeApiServiceGenerator.FAKE_USERS_RANDOM.random()
        if (!_users.contains(randomUser)) {
            _users.add(randomUser)
        }
    }

    override fun deleteUser(user: User) {
        _users.remove(user)
    }
}