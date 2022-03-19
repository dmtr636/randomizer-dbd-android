package com.kodimstudio.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.model.News
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class NewsListAdapter(
    private val news: List<News>,
    private val navController: NavController
) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = news[position]

        holder.newsTitle.text = news.title
        holder.newsDate.text = news.date

        val radius = 20
        val margin = 0
        val transformationTop: Transformation = RoundedCornersTransformation(radius, margin, RoundedCornersTransformation.CornerType.TOP)
        val transformationBottom: Transformation = RoundedCornersTransformation(radius, margin, RoundedCornersTransformation.CornerType.BOTTOM)

        Picasso.get().load(news.imageUrl).transform(transformationTop).into(holder.newsImage)
        Picasso.get().load(R.drawable.news_bottom).transform(transformationBottom).into(holder.bottomBg);

        holder.itemView.setOnClickListener {
            val bundle = bundleOf("url" to news.url)
            navController.navigate(R.id.action_news_to_details, bundle)
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsImage: ImageView = view.findViewById(R.id.news_image)
        val bottomBg: ImageView = view.findViewById(R.id.news_bottom_bg)
        val newsTitle: TextView = view.findViewById(R.id.news_title)
        val newsDate: TextView = view.findViewById(R.id.news_date)
    }
}