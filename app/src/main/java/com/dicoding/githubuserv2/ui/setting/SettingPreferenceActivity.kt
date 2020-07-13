package com.dicoding.githubuserv2.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.service.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting_preference.*

class SettingPreferenceActivity : AppCompatActivity() {
    companion object{
        const val DARK_MODE = "dark"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_preference)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.commit {
            add(
                R.id.setting_holder,
                PreferenceFragment(),
                PreferenceFragment::class.java.simpleName
            )
        }

        fab_language.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private var alarmReceiver = AlarmReceiver()
    fun alarmCall() {
        alarmReceiver.setRepeatingAlarm(this, "09:00")
    }

    fun cancelAlarm() {
        alarmReceiver.cancelAlarm(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}