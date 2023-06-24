package com.example.comptest

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.comptest.di.Injection
import com.example.comptest.model.DataItem
import com.example.comptest.model.data.UsersRepository

class UserListViewModel(usersRepository: UsersRepository) : ViewModel() {
    var user: LiveData<PagingData<DataItem>> =
        usersRepository.getUser().cachedIn(viewModelScope)
}


class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserListViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}