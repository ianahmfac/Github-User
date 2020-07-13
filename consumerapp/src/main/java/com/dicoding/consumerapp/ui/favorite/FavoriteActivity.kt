package com.dicoding.consumerapp.ui.favorite

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI

class FavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val cursor = contentResolver.query(CONTENT_URI, null, null, null, null) as Cursor

        val mFragmentManager = supportFragmentManager
        val mHomeFavoriteFragment = HomeFavoriteFragment()
        val mListFavoriteFragment = ListFavoriteFragment()
        val mFragment: Fragment
        val fragment: String
        if (cursor.count > 0) {
            mFragment = mListFavoriteFragment
            fragment = ListFavoriteFragment::class.java.simpleName
        } else {
            mFragment = mHomeFavoriteFragment
            fragment = HomeFavoriteFragment::class.java.simpleName
        }
        mFragmentManager.commit {
            add(R.id.activity_favorite, mFragment, fragment)
        }
    }
}