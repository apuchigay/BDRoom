package com.example.clase20243009.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clase20243009.Model.User

@Dao
interface UserDao {

    // suspend evita que la operacion falle cuando se realicen operaciones asincronas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}