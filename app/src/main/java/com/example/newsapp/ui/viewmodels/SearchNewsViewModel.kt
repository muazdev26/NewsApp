package com.example.newsapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.networking.NewsResponse
import com.example.newsapp.repository.AppRepository
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchNewsViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    var searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
//    var searchNewsResponse: NewsResponse? = null

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(Resource.Loading())
        val response = appRepository.searchNews(searchQuery, searchNewsPage)
        searchNewsLiveData.postValue(handleSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
//                searchNewsPage++
//                searchNewsResponse = newsResponse
//                if (searchNewsResponse == null) {
//                } else {
//                    val oldNews = searchNewsResponse?.articles
//                    val newNews = newsResponse.articles
//                    oldNews?.addAll(newNews)
//                }
                return Resource.Success(newsResponse)
            }
        }
        return Resource.Failure(response.message())
    }
}
