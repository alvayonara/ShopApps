package com.alvayonara.shopapps.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alvayonara.shopapps.core.utils.ConstPreferences.PREF_NAME
import com.alvayonara.shopapps.core.utils.ConstPreferences.PRIVATE_MODE
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * View visibility
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.snack(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

/**
 * Swipe Refresh state
 */
fun SwipeRefreshLayout.hideLoading() {
    this.isRefreshing = false
}

/**
 * Date Time converter
 */
@SuppressLint("SimpleDateFormat")
fun String?.convertDateTime(
    inputFormat: String = "yyyy-MM-dd",
    outputFormat: String = "dd MMMM yyyy"
): String {
    return try {
        val sdf = SimpleDateFormat(inputFormat)
        val convertDate = sdf.parse(this.orEmpty())
        SimpleDateFormat(outputFormat).format(convertDate!!)
    } catch (e: ParseException) {
        ""
    }
}

/**
 * Intent configuration
 */
inline fun <reified T : AppCompatActivity> Context.moveActivity() {
    Intent(this, T::class.java).also { intent ->
        startActivity(intent)
    }
}

inline fun <reified T : AppCompatActivity> Context.moveActivityClearTask() {
    Intent(this, T::class.java).also { intent ->
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

inline fun <reified T : AppCompatActivity> Context.moveActivity(bundle: Bundle) {
    Intent(this, T::class.java).also { intent ->
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

/**
 * Shared Preferences
 */
fun setDataPreferenceString(context: Context, key: String, value: String) {
    val sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    val editor = sharedPref.edit()
    with(editor) {
        putString(key, value)
        apply()
    }
}

fun setDataPreferenceBoolean(context: Context, key: String, value: Boolean) {
    val sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    val editor = sharedPref.edit()
    with(editor) {
        putBoolean(key, value)
        apply()
    }
}

fun getDataPreferenceString(context: Context, key: String): String? {
    val sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    return sharedPref.getString(key, "")
}

fun getDataPreferenceBoolean(context: Context, key: String): Boolean {
    val sharedPref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    return sharedPref.getBoolean(key, false)
}