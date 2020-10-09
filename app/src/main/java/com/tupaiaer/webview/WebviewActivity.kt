package com.tupaiaer.webview

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webview.*
import java.net.URISyntaxException

class WebviewActivity : AppCompatActivity() {

    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        Handler().postDelayed({
            ll_animation.visibility = View.GONE
        }, 5000)

        val url: String = intent.getStringExtra("url")

        if (savedInstanceState == null) {
            web_view!!.loadUrl("http://$url")
        }

        web_view!!.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Toast.makeText(applicationContext, url.toString(), Toast.LENGTH_SHORT).show()
                if (url!!.startsWith("https") || url.startsWith("http")) {
                    return false
                } else {
                    try {
                        val i = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        val fallbackUrl: String? = i.getStringExtra("browser_fallback_url")
                        Log.v("Fallback URL", fallbackUrl.toString())
                        if (fallbackUrl != null) {
                            view?.loadUrl(fallbackUrl)
                            return true
                        }
                    } catch (e: URISyntaxException) {
                        Log.e("URI Syntax", e.reason)
                    }
                }
                return true
            }
        }

        web_view.settings.builtInZoomControls = true
        web_view.settings.javaScriptEnabled = true
        web_view.settings.domStorageEnabled = true
        web_view.settings.databaseEnabled = true
        web_view.settings.minimumFontSize = 1
        web_view.settings.minimumLogicalFontSize = 1

        sdkCheck()
    }


    private fun sdkCheck() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    override fun onPause() {
        super.onPause()

        val activityManager = applicationContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        activityManager.moveTaskToFront(taskId, 0)
    }

    override fun onBackPressed() {
        val webView = findViewById<WebView>(R.id.web_view)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Ketuk sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        web_view.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        web_view.restoreState(savedInstanceState)
    }
}
