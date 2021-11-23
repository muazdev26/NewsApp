package com.example.newsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.networking.Article

class ArticlesAdapter(
    private var newsList : List<Article>,
    private val onItemClickListener: AdapterView.OnItemClickListener
) : RecyclerView.Adapter<ArticlesAdapter.ArticlesHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticlesHolder(
        ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ArticlesHolder, position: Int) {
        holder.bind(newsList[position])
    }

    inner class ArticlesHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        @SuppressLint("SetTextI18n")
        fun bind(article: Article) {
            binding.apply {
                article.apply {
                    tvTitle.text = title
                    tvDescription.text = description
                    tvPublishedAt.text = publishedAt.substring(0..9)
                    ivArticle.load(urlToImage) {
                        placeholder(R.drawable.placeholder)
                        crossfade(true)
                        crossfade(300)
                        error(R.drawable.placeholder)
                    }
                    itemView.setOnClickListener {
                        onItemClickListener.onItemClick(null, itemView, adapterPosition, 0)
                    }
                }
            }
        }
    }

    override fun getItemCount() = newsList.size
}