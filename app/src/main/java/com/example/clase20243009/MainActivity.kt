package com.example.clase20243009

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.clase20243009.DAO.UserDao
import com.example.clase20243009.Database.UserDatabase
import com.example.clase20243009.Screen.UserApp

class MainActivity : ComponentActivity() {
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = UserDatabase.getDatabase(applicationContext)
        userDao = db.userDao()
        enableEdgeToEdge()
        setContent {
            UserApp(userDao)
        }
    }
}
