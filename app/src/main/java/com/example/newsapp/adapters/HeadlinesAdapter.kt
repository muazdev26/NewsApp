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
import com.example.newsapp.databinding.ItemHeadlineBinding
import com.example.newsapp.networking.Article

class HeadlinesAdapter(
    private val onItemClickListener: AdapterView.OnItemClickListener
) : ListAdapter<Article, HeadlinesAdapter.HeadLinesHolder>(ArticleDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HeadLinesHolder(
        ItemHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: HeadLinesHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class HeadLinesHolder(private val binding: ItemHeadlineBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        @SuppressLint("SetTextI18n")
        fun bind(article: Article) {
            binding.apply {
                article.apply {
                    tvHeadlineTitle.text = title
                        ivHeadLine.load(urlToImage) {
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

    private class ArticleDiffUtil : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem

    }

}