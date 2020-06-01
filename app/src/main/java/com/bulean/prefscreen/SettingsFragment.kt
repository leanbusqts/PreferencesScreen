package com.bulean.prefscreen

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar


private const val TAG = "MainActivity"
private const val REQUEST_CODE = 34

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    var switchState: Boolean = false
    var thisSettingsFragment: SettingsFragment = this

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Key = text
        val textPreference: EditTextPreference? = findPreference("text")
        textPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_TEXT
        }
        // Key = counting
        val countingPreference: EditTextPreference? = findPreference("counting")
        countingPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "Not set"
                } else {
                    "Length of saved value: " + text.length
                }
            }
        // Key = number
        val numberPreference: EditTextPreference? = findPreference("number")
        numberPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
        // Key = feedback
        val feedback: Preference? = findPreference("feedback")
        feedback?.setOnPreferenceClickListener {
            showSnackbar(thisSettingsFragment)
            true
        }

        // Key = permissions
        val permissions: Preference? = findPreference("permissions")
        permissions?.setOnPreferenceClickListener {
            //showSnackbar(thisSettingsFragment)
            requestLocationPermissions()
            true
        }
    }

    private fun showSnackbar(view: SettingsFragment) {
        val snackbar = Snackbar
            .make(requireView(), "CLICK", Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    /*
    https://developer.android.com/guide/topics/ui/settings/use-saved-values
    https://developer.android.com/reference/kotlin/android/content/SharedPreferences
    https://developer.android.com/guide/topics/data/data-storage#pref
    */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val switchPref = sharedPreferences.getBoolean("switch", false)
        switchState = switchPref
        Toast.makeText(context, "Switch $switchState", Toast.LENGTH_SHORT).show()

        val checkPref = sharedPreferences.getBoolean("checkbox", false)
        if(checkPref){
            Toast.makeText(context, "CHECKBOX ON", Toast.LENGTH_SHORT).show()
        }
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    private fun requestLocationPermissions() {
        val provideRationale = foregroundPermissionApproved()
        if (provideRationale) {
            Snackbar.make(
                requireView(),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }
    }
}