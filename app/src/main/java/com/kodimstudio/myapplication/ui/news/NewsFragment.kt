package com.kodimstudio.myapplication.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodimstudio.myapplication.adapters.NewsListAdapter
import com.kodimstudio.myapplication.databinding.FragmentNewsBinding
import com.kodimstudio.myapplication.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.topBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            binding.newsList.updatePadding(
                bottom = insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        val newsList = mutableListOf<News>()
        val adapter = NewsListAdapter(newsList, findNavController())
        binding.newsList.adapter = adapter
        binding.newsList.layoutManager = LinearLayoutManager(requireContext())

        loadNews(newsList, adapter)

        return binding.root
    }

    private fun loadNews(newsList: MutableList<News>, adapter: NewsListAdapter) {
        val url = "https://deadbydaylight.com/en/news"

        lifecycleScope.launch(Dispatchers.IO) {
            val httpCacheDirectory = File(requireContext().cacheDir, "http-cache")
            val cacheSize = 1 * 1024 * 1024 // 1 MiB
            val cache = Cache(httpCacheDirectory, cacheSize.toLong())
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(CacheInterceptor())
                .cache(cache)
                .build()
            val request: Request = Request.Builder().url(url).get().build()
            val doc = Jsoup.parse(okHttpClient.newCall(request).execute().body!!.string())
            val items = doc.select(".news-list__items .news-list__item")
            for (item: Element in items) {
                val link = item.selectFirst("a")
                val newsUrl = link.attr("href")
                val linkStyle = link.attr("style")
                val imageUrl = linkStyle
                    .replace("background-image:url('", "https://s3.deadbydaylight.com")
                    .replace("')", "")
                val title = item.selectFirst(".news-list__title").text()
                val date = item.selectFirst(".news-list__date").text()

                newsList.add(News(
                    imageUrl = imageUrl,
                    title = title,
                    date = date,
                    url = newsUrl))
            }
            withContext(Dispatchers.Main) {
                adapter.notifyItemRangeInserted(0, newsList.size)
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    class CacheInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response = chain.proceed(chain.request())
            val cacheControl: CacheControl = CacheControl.Builder()
                .maxAge(15, TimeUnit.MINUTES) // 15 minutes cache
                .build()
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }
}