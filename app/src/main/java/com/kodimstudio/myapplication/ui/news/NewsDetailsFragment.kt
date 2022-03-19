package com.kodimstudio.myapplication.ui.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.kodimstudio.myapplication.databinding.FragmentNewsDetailsBinding

class NewsDetailsFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailsBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.topBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }

        val myWebView: WebView = binding.webView
        myWebView.settings.javaScriptEnabled = true
        val url = arguments?.get("url").toString()
        myWebView.loadUrl(url)

        for (i in 1..20) {
            myWebView.postDelayed({
                myWebView.loadUrl("javascript:document.getElementsByTagName(\"footer\")[0].setAttribute(\"style\",\"display:none;\");")
                myWebView.loadUrl("javascript:document.getElementsByTagName(\"header\")[0].setAttribute(\"style\",\"display:none;\");")
                myWebView.loadUrl("javascript:document.getElementById(\"app\").setAttribute(\"style\",\"padding-top:0;\");")
            }, (i*250).toLong())
        }

        return binding.root
    }
}