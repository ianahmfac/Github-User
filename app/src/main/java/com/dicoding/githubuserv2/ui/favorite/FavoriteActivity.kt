package com.dicoding.githubuserv2.ui.favorite

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserv2.ui.setting.SettingPreferenceActivity

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            val mIntent = Intent(this, SettingPreferenceActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}