package com.example.comptest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comptest.adapter.LoadingStateAdapter
import com.example.comptest.adapter.UsersAdapter
import com.example.comptest.databinding.ActivityThirdScreenBinding
import com.example.comptest.model.DataItem

class ThirdScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdScreenBinding
    private val userListViewModel: UserListViewModel by viewModels {
        ViewModelFactory(this)
    }
    private lateinit var userAdapter: UsersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userAdapter = UsersAdapter()
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        getUsers()
        pullRefresh()
    }

    private fun pullRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            userAdapter.refresh()
        }
        userAdapter.addLoadStateListener {
            val refreshing = it.refresh is LoadState.Loading
            binding.swipeRefresh.isRefreshing = refreshing
        }
    }

    private fun getUsers() {
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@ThirdScreenActivity)
            adapter = userAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    userAdapter.retry()
                }
            )

        }
        userAdapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: DataItem?) {
                val intent = Intent()
                intent.putExtra(
                    SecondScreenActivity.EXTRA_USERNAME,
                    "${user?.firstName} ${user?.lastName}"
                )
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
        userListViewModel.user.observe(this) {
            userAdapter.submitData(lifecycle, it)

        }
    }
}