package com.dicoding.consumerapp.ui.user

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.adapter.SectionPagerAdapter
import com.dicoding.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMN_NAME
import com.dicoding.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMN_PHOTO_USER
import com.dicoding.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.COLUMN_USERNAME
import com.dicoding.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.consumerapp.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUser : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private var statusFavorite = false
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_USERNAME = "username"
        const val EXTRA_PHOTO = "photo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val photo = intent.getStringExtra(EXTRA_PHOTO)

        supportActionBar?.title = username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(viewPager)
        supportActionBar?.elevation = 0f

        Glide.with(this).load(photo).apply(RequestOptions().override(120, 120)).into(imgPhoto)
        showDetailItem(username)

        uriWithId = Uri.parse("$CONTENT_URI/username")
        Log.d("Cek", uriWithId.toString())

        val cursor = contentResolver.query(uriWithId, null, username, null, null)
        Log.d("Cek", cursor.toString())
        statusFavorite = cursor?.count != 0

        setStatusFavorite(statusFavorite)
        fabFavorite.setOnClickListener {
            statusFavorite = !statusFavorite
            if (statusFavorite) {
                addFavorite(username, photo)
            } else {
                deleteFavorite(username)
            }
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            fabFavorite.setImageResource(R.drawable.favorite)
        } else {
            fabFavorite.setImageResource(R.drawable.unfavorite)
        }
    }

    private fun addFavorite(username: String, photo: String) {
        val name = tvName.text.toString()

        //insert values to db
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PHOTO_USER, photo)

        contentResolver.insert(CONTENT_URI, values)
        Snackbar.make(
            activity_detail_user,
            getString(R.string.addFavorite),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun deleteFavorite(username: String) {
        contentResolver.delete(uriWithId, username, null)
        Snackbar.make(
            activity_detail_user,
            getString(R.string.deleteFavorite),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showDetailItem(userame: String) {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        showLoading(true)
        detailViewModel.setUser(userame)

        detailViewModel.getUser().observe(this, Observer { userItem ->
            if (userItem != null) {
                val data = userItem[0]
                tvName.text = if (data.name.equals("null")) "-" else data.name
                tvCompany.text = if (data.company.equals("null")) "-" else data.company
                tvLocation.text = if (data.location.equals("null")) "-" else data.location
                tvRepositoryValue.text = data.repository.toString()
                tvFollowersValue.text = data.followers.toString()
                tvFollowingValue.text = data.following.toString()
                showLoading(false)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) progressBarDetail.visibility = View.VISIBLE
        else progressBarDetail.visibility = View.GONE
    }
}