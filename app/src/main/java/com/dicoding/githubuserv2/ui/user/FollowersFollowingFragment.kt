package com.dicoding.githubuserv2.ui.user

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
import com.dicoding.githubuserv2.adapter.FollowAdapter
import com.dicoding.githubuserv2.model.UserFollow
import com.dicoding.githubuserv2.viewmodel.FollowersViewModel
import com.dicoding.githubuserv2.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_followers_following.*

class FollowersFollowingFragment : Fragment() {

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val EXTRA_USERNAME = "username"

        fun newInstance(index: Int): FollowersFollowingFragment {
            val fragment =
                FollowersFollowingFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var adapter: FollowAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = activity?.intent?.getStringExtra(EXTRA_USERNAME) as String

        var index = 1
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }
        when (index) {
            1 -> {
                showFollowList()
                followersItem(username)
            }
            else -> {
                showFollowList()
                followingItem(username)
            }
        }
    }

    private fun showFollowList() {
        adapter = FollowAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object : FollowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFollow) {
                val intentDetail = Intent(activity, DetailUser::class.java)
                intentDetail.putExtra(DetailUser.EXTRA_USERNAME, data.usernameFollow)
                intentDetail.putExtra(DetailUser.EXTRA_PHOTO, data.photoFollow)
                startActivity(intentDetail)
            }
        })
    }

    private fun followersItem(username: String) {
        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)

        showLoading(true)
        followersViewModel.setFollowers(username)

        followersViewModel.getFollowers().observe(viewLifecycleOwner, Observer { followersItem ->
            if (followersItem != null) {
                adapter.setData(followersItem)
                showLoading(false)
            }
        })
    }

    private fun followingItem(username: String) {
        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        showLoading(true)
        followingViewModel.setFollowing(username)

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingItem ->
            if (followingItem != null) {
                adapter.setData(followingItem)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }
}