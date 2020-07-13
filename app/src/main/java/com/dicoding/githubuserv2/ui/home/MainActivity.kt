package com.dicoding.githubuserv2.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.ui.favorite.FavoriteActivity
import com.dicoding.githubuserv2.ui.setting.SettingPreferenceActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clickSearchUser()
        getFragment()

        fab_menu_favorite.setOnClickListener {
            val mIntent = Intent(this, FavoriteActivity::class.java)
            startActivity(mIntent)
        }
    }

    private fun clickSearchUser() {
        searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) return true
                goToListFragment(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getFragment() {
        val mFragmentManager = supportFragmentManager
        val mHomeFragment = HomeFragment()
        val fragment = mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment !is HomeFragment) {
            mFragmentManager.commit {
                add(R.id.containerFragment, mHomeFragment, HomeFragment::class.java.simpleName)
            }
        }
    }

    private fun goToListFragment(query: String) {
        val mListUserFragment = ListUserFragment()
        val mBundle = Bundle()
        mBundle.putString(ListUserFragment.USERNAME, query)
        mListUserFragment.arguments = mBundle

        val mFragmentManager = supportFragmentManager
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.containerFragment,
                mListUserFragment,
                ListUserFragment::class.java.simpleName
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val mIntent = Intent(this, SettingPreferenceActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}