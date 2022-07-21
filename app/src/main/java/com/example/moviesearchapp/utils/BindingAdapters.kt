package com.example.moviesearchapp.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@BindingAdapter(value = ["loadImage", "placeHolder", "error"], requireAll = false)
fun ImageView.loadImage(
    imageUrl: String,
    @DrawableRes
    placeholder: Int = 0,
    @DrawableRes
    error: Int = 0,
) {
    // imageUrl 이 비어있을 때 로직 추가
    if (imageUrl.isEmpty()) {
        if (placeholder != 0) {
            this.setImageResource(placeholder)
        }

        return
    }

    val options = RequestOptions()

    @SuppressLint("CheckResult")
    if (error != 0) {
        // 로드가 실패할 경우 표시할 리소스를 설정합니다.
        options.error(error)
    }

    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(imageUrl).apply {
            @SuppressLint("CheckResult")
            if (placeholder != 0) {
                // 리소스가 로드되는 동안 표시할 드로어블 리소스의 Android 리소스 ID를 설정합니다.
                apply(options.placeholder(placeholder))
            }
        }
        .into(object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                setImageDrawable(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                setImageDrawable(placeholder)
            }
        })
}

// 중복 클릭 방지
@BindingAdapter(value = ["onSingleClick", "interval"], requireAll = false)
fun View.onSingleClick(listener: View.OnClickListener? = null, interval: Long? = null) {
    if (listener != null) {
        setOnClickListener(object : Listener.OnSingleClickListener(interval ?: 1000L) {
            override fun onSingleClick(v: View?) {
                listener.onClick(v)
            }
        })
    } else {
        setOnClickListener(null)
    }
}

@BindingAdapter("htmlToString")
fun TextView.htmlToString(text: String) {
    this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(text).toString()
    }
}

@BindingAdapter(value = ["setOnFocusChangeListener"])
fun View.setOnFocusChangeListener(focusListener: View.OnFocusChangeListener) {
    onFocusChangeListener = focusListener
}

@BindingAdapter(value = ["addTextChangedListener"])
fun TextView.addTextChangedListener(watcher: TextWatcher) {
    removeTextChangedListener(watcher)
    addTextChangedListener(watcher)
}