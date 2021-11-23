package com.example.newsapp.utils

import com.example.newsapp.R

object CategoryUtils {

    fun getCategories(): List<Category> {
        val categoryList = mutableListOf<Category>()
        categoryList.add(Category("Education", R.drawable.ic_education))
        categoryList.add(Category("Politics", R.drawable.ic_politics))
        categoryList.add(Category("Sports", R.drawable.ic_sports))
        categoryList.add(Category("Technology", R.drawable.ic_technology))
        categoryList.add(Category("Crypto", R.drawable.ic_bitcoin))
        categoryList.add(Category("Business", R.drawable.ic_business))
        categoryList.add(Category("Science", R.drawable.ic_science))
        categoryList.add(Category("Health", R.drawable.ic_health))
        categoryList.add(Category("Entertainment", R.drawable.ic_entertainment))
        return categoryList
    }
}

data class Category(
    var categoryName: String,
    var categoryImage: Int
)