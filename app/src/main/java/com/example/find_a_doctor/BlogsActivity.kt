package com.example.find_a_doctor

import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.example.find_a_doctor.BaseActivity

class BlogsActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Find the content frame and inflate the specific layout
        val contentFrame: FrameLayout = findViewById(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_blogs, contentFrame, true)

        // Set the header title
        setHeaderTitle("HealthCare Blogs")

        // Initialize the WebView
        val webView: WebView = findViewById(R.id.web_view)

        // Set up WebView
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoading()  // Show progress bar when page starts loading
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideLoading()  // Hide progress bar when page finishes loading
            }
        }

        webView.settings.javaScriptEnabled = true
        // Load the URL
        webView.loadUrl("https://www.marham.pk/healthblog")
    }

    override fun customizeHeader() {
        // Customize header for BlogsActivity if needed
    }
}
