package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.networking.Article
import com.example.newsapp.networking.NewsResponse
import com.example.newsapp.repository.AppRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    var allHeadLinesLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    var searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getAllHeadLines("us")
    }

    fun getAllHeadLines(countryCode: String) = viewModelScope.launch {
        allHeadLinesLiveData.postValue(Resource.Loading())
        val response = appRepository.getAllHeadLines(countryCode, breakingNewsPage)
        allHeadLinesLiveData.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(Resource.Loading())
        val response = appRepository.searchNews(searchQuery, searchNewsPage)
        searchNewsLiveData.postValue(handleSearchNewsResponse(response))
    }

    fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = newsResponse
                } else {
                    val oldNews = breakingNewsResponse?.articles
                    val newNews = newsResponse.articles
                    oldNews?.addAll(newNews)
                }
                return Resource.Success(breakingNewsResponse ?: newsResponse)
            }
        }
        return Resource.Failure(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = newsResponse
                } else {
                    val oldNews = searchNewsResponse?.articles
                    val newNews = newsResponse.articles
                    oldNews?.addAll(newNews)
                }
                return Resource.Success(searchNewsResponse ?: newsResponse)
            }
        }
        return Resource.Failure(response.message())
    }

    fun saveNews(article: Article) = viewModelScope.launch {
        appRepository.addArticle(article)
    }

    suspend fun isNewsExists(articleURL: String): Boolean {
        val job = CoroutineScope(Dispatchers.IO).async {
            appRepository.isArticleExists(articleURL)
        }
        return job.await()
    }

    fun deleteNews(article: Article) = viewModelScope.launch {
        appRepository.deleteArticle(article)
    }

    fun getSavedNews() = appRepository.getAllArticles()

}