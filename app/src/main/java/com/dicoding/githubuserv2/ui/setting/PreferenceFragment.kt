package com.dicoding.githubuserv2.ui.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.githubuserv2.R
import com.dicoding.githubuserv2.ui.home.MainActivity

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var reminder: String
    private lateinit var darkMode: String

    private lateinit var reminderPreference: SwitchPreference
    private lateinit var darkModePreference: SwitchPreference

    private var isDark: Boolean = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        reminderPreference.isChecked = sh.getBoolean(reminder, false)
        darkModePreference.isChecked = sh.getBoolean(darkMode, false)
        isDark = sh.getBoolean(darkMode, false)

        if(isDark){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun init() {
        reminder = resources.getString(R.string.key_reminder)
        darkMode = "darkMode"
        reminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference
        darkModePreference = findPreference<SwitchPreference>(darkMode) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == reminder) {
            reminderPreference.isChecked =
                sharedPreferences.getBoolean(reminder, false)

            val activity = activity as SettingPreferenceActivity
            if (reminderPreference.isChecked) {
                activity.alarmCall()
            } else {
                activity.cancelAlarm()
            }
        }

        if (key == darkMode) {
            if (darkModePreference.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}