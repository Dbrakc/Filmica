package com.davidbragadeveloper.filmica.data.repos

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.davidbragadeveloper.filmica.data.ApiRoutes
import com.davidbragadeveloper.filmica.data.Film

object TrendingFilmsRepo : BaseFilmsRepo() {


    fun trendingFilms(
        context: Context,
        callbackSucces: ((MutableList<Film>)->Unit),
        callbackError: ((VolleyError)->Unit),
        callbackNoResults: () -> Unit
    ){
        if(films.isEmpty()) {
            requestTrendingFilms(
                callbackSucces,
                callbackError,
                callbackNoResults,
                context
            )
        }else {
            callbackSucces.invoke(films)
        }

    }

    private fun requestTrendingFilms(
        callbackSucces: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        callbackNoResults: () -> Unit,
        context: Context) {
        val url = ApiRoutes.trendingURL()
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                films.addAll(
                    Film.parseFilms(
                        it
                    )
                )
                if(!films.isEmpty()) {
                    callbackSucces.invoke(films)
                }else{
                    callbackNoResults.invoke()
                }
            },
            {
                callbackError.invoke(it)
            })
        Volley.newRequestQueue(context).add(request)

    }

    override fun findFilmById(context: Context, id: String, callbackSuccess: (Film) -> Unit) {
        val film = films.find { it.id==id }
        film?.let {
            callbackSuccess.invoke(it)
        }
    }

}