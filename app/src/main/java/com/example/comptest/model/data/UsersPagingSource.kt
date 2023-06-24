package com.example.comptest.model.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.comptest.model.DataItem
import com.example.comptest.model.api.ApiService

class UsersPagingSource(private val apiService: ApiService) : PagingSource<Int, DataItem>(){
    private companion object {
        const val INIT_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItem> {
        return try {
            val position = params.key ?: INIT_PAGE_INDEX
            val response = apiService.getUsers(
                position,
                params.loadSize
            )
            LoadResult.Page(
                data = response.data,
                prevKey = if (position == INIT_PAGE_INDEX) null else position -1,
                nextKey = if (response.data.isNullOrEmpty()) null else position +1
            )

        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, DataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}