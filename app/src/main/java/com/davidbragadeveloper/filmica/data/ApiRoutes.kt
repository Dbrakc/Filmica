package com.davidbragadeveloper.filmica.data

import android.net.Uri
import com.davidbragadeveloper.filmica.BuildConfig

object ApiRoutes {

    fun discoverURL(language: String = "en-US",
                    sortedBy: String = "popularity.desc",
                    page: Int = 1): String{
        return getUriBuilder()
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("language",language)
            .appendQueryParameter("sort_by",sortedBy)
            .appendQueryParameter("page",page.toString())
            .appendQueryParameter("include_adult","false")
            .appendQueryParameter("include_vide","false")
            .build()
            .toString()
    }

    private fun getUriBuilder () = Uri.Builder()
        .scheme("https")
        .authority("api.themoviedb.org")
        .appendPath("3")
        .appendQueryParameter("api_key", BuildConfig.MovieDBApiKey)

    fun trendingURL(timeWindow: String = "Week"): String {
        return getUriBuilder()
            .appendPath("trending")
            .appendPath("movie")
            .appendPath(timeWindow)
            .build()
            .toString()
    }
}