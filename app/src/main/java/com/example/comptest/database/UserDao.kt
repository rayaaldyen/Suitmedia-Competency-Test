package com.example.comptest.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.comptest.model.DataItem

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: List<DataItem>)

    @Query("SELECT * FROM users")
    fun getAllUser(): PagingSource<Int, DataItem>

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}