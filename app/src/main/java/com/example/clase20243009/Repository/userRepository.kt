package com.example.clase20243009.Repository

import com.example.clase20243009.DAO.UserDao
import com.example.clase20243009.Model.User

class userRepository(private val userDao: UserDao) {
    suspend fun insert(user: User){
        userDao.insert(user)
    }

    suspend fun getAllUser(): List<User>{
        return userDao.getAllUsers()
    }
}