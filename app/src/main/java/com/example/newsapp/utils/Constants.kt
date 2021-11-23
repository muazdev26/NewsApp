package com.example.newsapp.utils

import java.util.*

object Constants {

    const val BASE_URL = "https://newsapi.org/v2/"
    const val SEARCH_NEWS_TIME_DELAY = 2000L
    const val QUERY_PAGE_SIZE = 20

    const val TAG = "myTag"

    fun getGreetingMessage():String{
        val c = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning!"
            in 12..15 -> "Good Afternoon!"
            in 16..20 -> "Good Evening!"
            in 21..23 -> "Good Night!"
            else -> "Welcome!"
        }
    }
}