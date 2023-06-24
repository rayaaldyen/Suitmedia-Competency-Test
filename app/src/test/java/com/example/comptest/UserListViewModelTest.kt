package com.example.comptest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.comptest.adapter.UsersAdapter
import com.example.comptest.model.DataItem
import com.example.comptest.model.data.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var usersRepository: UsersRepository

    @Test
    fun `when Get User Should Not Null and Return Data`() = runTest {
        val dummyUser = DummyData.generateDummyUserResponse()
        val data: PagingData<DataItem> = UserPagingSource.snapshot(dummyUser)
        val expectedUserList = MutableLiveData<PagingData<DataItem>>()
        expectedUserList.value = data
        Mockito.`when`(usersRepository.getUser()).thenReturn(expectedUserList)

        val userListVieModel = UserListViewModel(usersRepository)
        val actualUserList: PagingData<DataItem> = userListVieModel.user.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = UsersAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualUserList)
        assertNotNull(differ.snapshot())
        assertEquals(dummyUser.size, differ.snapshot().size)
        assertEquals(dummyUser[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get User Empty Should Return No Data`() = runTest {
        val data: PagingData<DataItem> = PagingData.from(emptyList())
        val expectedUserList = MutableLiveData<PagingData<DataItem>>()
        expectedUserList.value = data
        Mockito.`when`(usersRepository.getUser()).thenReturn(expectedUserList)
        val userListViewModel = UserListViewModel(usersRepository)
        val actualUserList: PagingData<DataItem> = userListViewModel.user.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = UsersAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualUserList)
        assertEquals(0, differ.snapshot().size)
    }
}
val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class UserPagingSource : PagingSource<Int, LiveData<List<DataItem>>>() {
    companion object {
        fun snapshot(items: List<DataItem>): PagingData<DataItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<DataItem>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<DataItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}