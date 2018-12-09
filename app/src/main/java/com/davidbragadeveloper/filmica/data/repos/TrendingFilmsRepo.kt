package com.davidbragadeveloper.filmica.data.repos

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.davidbragadeveloper.filmica.data.ApiRoutes
import com.davidbragadeveloper.filmica.data.Film

object TrendingFilmsRepo : BaseFilmsRepo() {
    fun trendingFilms(context: Context, callbackSucces: ((MutableList<Film>)->Unit), callbackError: ((VolleyError)->Unit)){
        if(films.isEmpty()) {
            requestTrendingFilms(
                callbackSucces,
                callbackError,
                context
            )
        }else {
            callbackSucces.invoke(films)
        }

    }

    private fun requestTrendingFilms(
        callbackSucces: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        context: Context)
    {
        val url = ApiRoutes.trendingURL()
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                films.addAll(
                    Film.parseFilms(
                        it
                    )
                )
                callbackSucces(films)
            },
            {
                callbackError.invoke(it)
            })
        Volley.newRequestQueue(context).add(request)

    }

}