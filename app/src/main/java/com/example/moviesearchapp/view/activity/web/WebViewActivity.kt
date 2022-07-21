package com.example.moviesearchapp.view.activity.web

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.webkit.*
import androidx.activity.viewModels
import com.example.moviesearchapp.R
import com.example.moviesearchapp.databinding.ActivityWebViewBinding
import com.example.moviesearchapp.base.BaseActivity
import com.example.moviesearchapp.utils.observeInLifecycleStop
import com.example.moviesearchapp.view.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WebViewActivity : BaseActivity<ActivityWebViewBinding, WebViewViewModel>(
    R.layout.activity_web_view
) {
    override val viewModel: WebViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = this.viewModel
        val url = intent.getStringExtra(PAGE_URL)
        val title = intent.getStringExtra(PAGE_TITLE) ?: ""
        val showTitle = intent.getBooleanExtra(PAGE_SHOWTITLE, false)

        if (url == null) {
            finish()
            return
        }

        initWebView(binding.webView)

        if (savedInstanceState == null) {
            binding.webView.loadUrl(url)
        }

        viewModel.title.value = title
        viewModel.showTitle.value = showTitle

        initViewModelCallback()
    }

    private fun initWebView(webView: WebView) {
        initWebViewSetting(webView)

        webView.webViewClient = webViewClient
        webView.webChromeClient = webChromeClient
    }

    private fun initWebViewSetting(webView: WebView) {
        val settings = webView.settings
        settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            useWideViewPort = true
            setSupportMultipleWindows(true)
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            databaseEnabled = true
        }
    }

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String,
        ): Boolean {
            return urlLoading(view, url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest,
        ): Boolean {
            val url = request.url.toString()
            return urlLoading(view, url)
        }

        private fun urlLoading(
            view: WebView,
            url: String,
        ): Boolean {
            if (!URLUtil.isNetworkUrl(url) && !URLUtil.isJavaScriptUrl(url)) {
                val uri = try {
                    Uri.parse(url)
                } catch (e: Exception) {
                    return false
                }

                return try {
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                    true
                } catch (e: Exception) {
                    false
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                view.loadUrl(url)
            }
            return true
        }

        @Suppress("DEPRECATION")
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String,
            failingUrl: String?,
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError,
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun onPageStarted(
            view: WebView?,
            url: String,
            favicon: Bitmap?,
        ) {
            super.onPageStarted(view, url, favicon)
            viewModel.showProgress.value = true
        }

        override fun onPageFinished(view: WebView?, url: String) {
            super.onPageFinished(view, url)
            viewModel.showProgress.value = false
        }
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message,
        ): Boolean {
            val newWebView = WebView(this@WebViewActivity)
            initWebView(newWebView)

            return true
        }

        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams,
        ): Boolean {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }
    }

    private fun initViewModelCallback() = with (viewModel) {
        close.onEach {
            onBackPressed()
        }.observeInLifecycleStop(this@WebViewActivity)
    }

    override fun onPause() {
        binding.webView.pauseTimers()
        binding.webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
        binding.webView.resumeTimers()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        binding.webView.saveState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.webView.restoreState(savedInstanceState)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            return
        }

        finish()
    }

    override fun onDestroy() {
        binding.webView.stopLoading()
        binding.webView.destroy()
        try {
            startActivity(Intent(this, MainActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            })
        } catch (e: Exception) {
            Timber.e("startActivity error : $e")
        }
        super.onDestroy()
    }

    companion object {
        private const val PAGE_URL = "pageUrl"
        private const val PAGE_TITLE = "pageTitle"
        private const val PAGE_SHOWTITLE = "showTitle"

        // 내부 웹뷰
        fun getInAppBrowserIntent(
            activity: Activity,
            url: String,
            pageTitle: String = "",
            showTitle: Boolean = true
        ): Intent {
            return Intent(activity, WebViewActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(PAGE_URL, url)
                putExtra(PAGE_TITLE, pageTitle)
                putExtra(PAGE_SHOWTITLE, showTitle)
            }
        }

        // 외부 웹뷰
        fun getExternalBrowserIntent(url: String): Intent {
            return Intent(Intent.ACTION_VIEW, Uri.parse(url)).addCategory(Intent.CATEGORY_BROWSABLE)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}