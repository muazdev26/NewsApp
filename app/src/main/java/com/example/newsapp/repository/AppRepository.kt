package com.example.newsapp.repository

import com.example.newsapp.database.ArticlesDatabase
import com.example.newsapp.networking.Article
import com.example.newsapp.networking.RetrofitInstance
import com.example.newsapp.utils.CategoryUtils
import com.example.newsapp.utils.CategoryUtils.getCategories

class AppRepository(
    private val database: ArticlesDatabase
) {

    suspend fun getAllHeadLines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getAllHeadLines(countryCode = countryCode, page = pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery = searchQuery, page = pageNumber)

    suspend fun addArticle(article: Article) = database.articlesDao().insertArticle(article)

    fun getAllArticles() = database.articlesDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = database.articlesDao().deleteArticle(article)

    suspend fun isArticleExists(articleURL: String) =
        database.articlesDao().isArticleExists(articleURL)

    fun getAllCategories() = getCategories()
}