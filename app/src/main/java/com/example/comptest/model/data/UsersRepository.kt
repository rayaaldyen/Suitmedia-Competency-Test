package com.example.comptest.model.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.comptest.database.UserDatabase
import com.example.comptest.model.DataItem
import com.example.comptest.model.api.ApiService

class UsersRepository(private val apiService: ApiService, private val userDatabase: UserDatabase) {
    fun getUser(): LiveData<PagingData<DataItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 6
            ),
            remoteMediator = UsersRemoteMediator(apiService, userDatabase),
            pagingSourceFactory = {
                userDatabase.userDao().getAllUser()
            }
        ).liveData
    }
}