package com.davidbragadeveloper.filmica.data.repos

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.davidbragadeveloper.filmica.data.api.ApiRoutes
import com.davidbragadeveloper.filmica.data.Film

object TrendingFilmsRepo : BaseFilmsRepo() {

    var lastPage = 1;

    fun trendingFilms(
        context: Context,
        page : Int = 1,
        callbackSucces: ((MutableList<Film>)->Unit),
        callbackError: ((VolleyError)->Unit),
        callbackNoResults: () -> Unit
    ){
        if(DiscoverFilmsRepo.films.isEmpty() || page > DiscoverFilmsRepo.lastPage) {
            requestTrendingFilms(
                page,
                callbackSucces,
                callbackError,
                callbackNoResults,
                context
            )
        }else {
            callbackSucces.invoke(films)
        }

        lastPage = page

    }

    private fun requestTrendingFilms(
        page: Int = 1,
        callbackSucces: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        callbackNoResults: () -> Unit,
        context: Context) {
        val url = ApiRoutes.trendingURL(page= page)
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