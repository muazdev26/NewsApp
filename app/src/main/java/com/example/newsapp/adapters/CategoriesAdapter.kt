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
import com.example.newsapp.databinding.ItemCategoryBinding
import com.example.newsapp.databinding.ItemHeadlineBinding
import com.example.newsapp.networking.Article
import com.example.newsapp.utils.Category

class CategoriesAdapter(
    private val onItemClickListener: AdapterView.OnItemClickListener
) : ListAdapter<Category, CategoriesAdapter.CategoryHolder>(CategoryDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryHolder(
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CategoryHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        @SuppressLint("SetTextI18n")
        fun bind(category: Category) {
            binding.apply {
                category.apply {
                    tvCategory.text = categoryName
                    ivCategory.load(categoryImage) {
                        crossfade(true)
                        crossfade(300)
                    }
                    itemView.setOnClickListener {
                        onItemClickListener.onItemClick(null, itemView, adapterPosition, 0)
                    }
                }
            }
        }
    }

    private class CategoryDiffUtil : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.categoryImage == newItem.categoryImage

        override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem

    }

}