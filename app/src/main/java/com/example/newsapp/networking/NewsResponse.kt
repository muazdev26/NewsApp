package com.example.newsapp.networking

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)