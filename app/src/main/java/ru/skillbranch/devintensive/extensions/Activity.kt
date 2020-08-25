package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import ru.skillbranch.devintensive.R

/* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
const val SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean {
    val rootView = window.decorView
    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)
    val dm: DisplayMetrics = rootView.resources.displayMetrics
    val heightDiff: Int = rootView.bottom - r.bottom

    return heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density
}

fun Activity.isKeyboardClosed(): Boolean {
    return !isKeyboardOpen()
}
