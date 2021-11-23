package com.example.newsapp.networking

import com.example.newsapp.utils.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getAllHeadLines(
        @Query("country") countryCode: String = "us",
        @Query("apiKey") apiKey: String = API_KEY.NEWS_API_KEY,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("apiKey") apiKey: String = API_KEY.NEWS_API_KEY,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>
}