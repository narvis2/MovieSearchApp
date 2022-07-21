package com.example.moviesearchapp.utils

import android.R
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

/**
 * OnGlobalLayoutListener -> View 의 전체 영역이 바뀔때 호출됨
 * 주의 : windowSoftInputMode 에 adjustNothing 을 사용하면 View 의 변경을 알 수 없어 Listener 가 동작하지 않음
 */
class KeyboardUtils private constructor(
    activity: Activity,
    private var mCallback: SoftKeyboardToggleListener?
) : ViewTreeObserver.OnGlobalLayoutListener {

    private val mRootView: View = (activity.findViewById<View>(R.id.content) as ViewGroup).getChildAt(0)
    private var prevValue: Boolean? = null
    private var dp = 0f
    private val mScreenDensity: Float = activity.resources.displayMetrics.density

    interface SoftKeyboardToggleListener {
        fun onToggleSoftKeyboard(isVisible: Boolean, height: Int)
    }

    init {
        mRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        mRootView.getWindowVisibleDisplayFrame(rect)
        val heightDiff: Int = mRootView.rootView.height - (rect.bottom - rect.top)
        dp = heightDiff / mScreenDensity
        val isVisible = dp > MAGIC_NUMBER
        if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible
            mCallback?.onToggleSoftKeyboard(isVisible, heightDiff)
        }
    }

    private fun removeListener() {
        mRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        mCallback = null
    }

    companion object {
        private const val MAGIC_NUMBER = 200
        private val sListenerMap: HashMap<SoftKeyboardToggleListener, KeyboardUtils> = HashMap()

        // Listener 등록
        fun addKeyboardToggleListener(act: Activity, listener: SoftKeyboardToggleListener) {
            removeKeyboardToggleListener(listener)
            sListenerMap[listener] = KeyboardUtils(act, listener)
        }

        // 등록된 Listener 제거
        fun removeKeyboardToggleListener(listener: SoftKeyboardToggleListener) {
            if (sListenerMap.containsKey(listener)) {
                val k = sListenerMap.remove(listener)
                k?.removeListener()
            }
        }
    }
}