package com.bulean.prefscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    /*
    * https://developer.android.com/guide/topics/ui/settings#kotlin
    * https://developer.android.com/reference/kotlin/androidx/preference/SwitchPreferenceCompat
    * https://developer.android.com/guide/topics/ui/settings/organize-your-settings
    * https://developer.android.com/guide/topics/ui/settings/components-and-attributes
    * https://developer.android.com/guide/topics/ui/settings/customize-your-settings
    * https://source.android.com/devices/tech/settings/settings-guidelines
    * https://developer.android.com/reference/kotlin/android/content/SharedPreferences.OnSharedPreferenceChangeListener
    * https://developer.android.com/reference/kotlin/android/content/SharedPreferences
    * https://developer.android.com/reference/kotlin/androidx/preference/PreferenceFragmentCompat
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }
}
