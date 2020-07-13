package com.dicoding.githubuserv2.ui.favorite

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.adapter.FavoriteAdapter
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserv2.model.Favorite
import com.dicoding.githubuserv2.ui.user.DetailUser
import kotlinx.android.synthetic.main.fragment_list_favorite.*

class ListFavoriteFragment : Fragment() {

    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showListFavorite()

        swipeRefresh.setOnRefreshListener {
            val cursor =
                context?.contentResolver?.query(CONTENT_URI, null, null, null, null) as Cursor

            if (cursor.count == 0) {
                parentFragmentManager.commit {
                    replace(
                        R.id.activity_favorite,
                        HomeFavoriteFragment(),
                        HomeFavoriteFragment::class.java.simpleName
                    )
                }
            } else {
                showListFavorite()
            }
            swipeRefresh.isRefreshing = false
        }
    }

    private fun showListFavorite() {
        adapter = FavoriteAdapter(context!!)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                val intentDetail = Intent(activity, DetailUser::class.java)
                intentDetail.putExtra(DetailUser.EXTRA_USERNAME, data.username)
                intentDetail.putExtra(DetailUser.EXTRA_PHOTO, data.photoUser)
                startActivity(intentDetail)
            }
        })
    }
}