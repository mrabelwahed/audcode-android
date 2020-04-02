package com.audcode.ui

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

import com.audcode.BaseApp
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.view_bottom_player.*


@Suppress("DEPRECATION")
fun Context.isServiceRunning(serviceClassName: String): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

    return activityManager?.getRunningServices(Integer.MAX_VALUE)?.any { it.service.className == serviceClassName }
        ?: false
}



fun Context.dpToPx(valueInDp: Float): Float {
    val metrics = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    currentFocus?.let {
        inputMethodManager?.hideSoftInputFromWindow(
            it.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

val Fragment.supportActionBar
    get() = (activity as? AppCompatActivity)?.supportActionBar

val Fragment.app
    get() = (activity as? AppCompatActivity)?.application as BaseApp

val Fragment.toolBar: Toolbar
    get() = (activity as AppCompatActivity)?.toolbar

val Fragment.bottomNavigation: BottomNavigationView
    get() = (activity as AppCompatActivity)?.bottomNavigationView

val Fragment.bottomPlayer :ConstraintLayout
    get() = (activity as AppCompatActivity)?.lastPlayedLayout

fun Fragment.openUrl(url: String): Boolean {
    try {
        val validUrl =
            if (!url.startsWith("www.") && !url.startsWith("http://") && !url.startsWith("https://")) {
                "http://www.$url"
            } else if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "http://$url"
            } else {
                url
            }

        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
            .launchUrl(context, Uri.parse(validUrl))

        return true
    } catch (e: Exception) {

        return false
    }
}

