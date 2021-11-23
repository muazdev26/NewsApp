package com.example.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.networking.Article

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Query("SELECT EXISTS(SELECT * FROM articles WHERE url=:articleURL)")
    suspend fun isArticleExists(articleURL: String): Boolean

    @Delete
    suspend fun deleteArticle(article: Article)

}