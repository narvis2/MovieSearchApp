package com.example.moviesearchapp.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.moviesearchapp.databinding.CustomToastBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

fun showSnackBar(
    view: View,
    msg: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    paddingVertical: Int = 10,
    paddingHorizontal: Int = 16,
) {
    if (!msg.isNullOrEmpty()) {
        try {
            Snackbar.make(view, "", duration).apply {
                val snackbarView = this.view
                (snackbarView as? Snackbar.SnackbarLayout)?.apply {
                    findViewById<TextView>(com.google.android.material.R.id.snackbar_text).visibility =
                        View.INVISIBLE
                    val binding = CustomToastBinding.inflate(
                        LayoutInflater.from(view.context),
                        null,
                        false
                    ).apply {
                        snackbarView.setPadding(
                            paddingHorizontal.pxToDp(view.context),
                            paddingVertical.pxToDp(view.context),
                            paddingHorizontal.pxToDp(view.context),
                            paddingVertical.pxToDp(view.context)
                        )

                        itemLl.visibility = View.VISIBLE
                        text.text = msg
                        text.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    }

                    setBackgroundColor(Color.TRANSPARENT)
                    addView(
                        binding.root,
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                } ?: setText(msg)
                show()
            }
        } catch (e: IllegalArgumentException) {
            Timber.e("${e.message}")
        }
    }
}

fun Int.pxToDp(context: Context): Int = this.toFloat().pxToDp(context).toInt()

fun Float.pxToDp(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun Context.hideKeyboard(view: View? = null) {
    if (view == null)
        return

    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)

    if (Build.VERSION.SDK_INT < 26) {
        imm.hideSoftInputFromInputMethod(view.windowToken, 0)
    }
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

fun String.htmlToString(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this).toString()
    }
}