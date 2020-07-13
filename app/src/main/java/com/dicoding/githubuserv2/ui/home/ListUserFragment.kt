package com.dicoding.githubuserv2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.adapter.UserAdapter
import com.dicoding.githubuserv2.model.User
import com.dicoding.githubuserv2.ui.user.DetailUser
import com.dicoding.githubuserv2.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list_user.*

class ListUserFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    companion object {
        var USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(USERNAME) as String
        showUserList()
        userItem(username)
    }

    private fun showUserList() {
        adapter = UserAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentDetail = Intent(activity, DetailUser::class.java)
                intentDetail.putExtra(DetailUser.EXTRA_USERNAME, data.username)
                intentDetail.putExtra(DetailUser.EXTRA_PHOTO, data.photo)
                startActivity(intentDetail)
            }
        })
    }

    private fun userItem(username: String) {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        showLoading(true)
        mainViewModel.setUser(username)

        mainViewModel.getUser().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                val data = userItems[0]
                if (data.userNotFound != null) {
                    Snackbar.make(activity_list, getString(R.string.notFound), Snackbar.LENGTH_LONG)
                        .show()
                    showLoading(false)
                } else {
                    adapter.setData(userItems)
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }
}