package com.prabel.github.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

fun EditText.stringText() = text.toString()

fun Activity.showSnackBar(message: CharSequence,
                          contentView: View = findViewById(android.R.id.content),
                          duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(contentView, message, duration).show()
}

fun Activity.showErrorSnackBar(message: CharSequence,
                               contentView: View = findViewById(android.R.id.content),
                               duration: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(contentView, message, duration)
    snackBar.view.setBackgroundColor(Color.parseColor("#EA343D"))

    val snackbarTextView = snackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
    snackbarTextView.textSize = 18f
    snackbarTextView.maxLines = 5
    snackbarTextView.setTextColor(Color.WHITE)
    snackBar.show()
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.setInVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, 0)
    }
}