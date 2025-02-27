package com.openclassrooms.magicgithub.api

import com.openclassrooms.magicgithub.model.User

interface ApiService {
    fun getUsers(): List<User>
    fun addRandomUser()
    fun deleteUser(user: User)
    fun updateUserState(user: User)
}