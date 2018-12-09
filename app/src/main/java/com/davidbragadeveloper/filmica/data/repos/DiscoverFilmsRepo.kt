package com.davidbragadeveloper.filmica.data.repos

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.davidbragadeveloper.filmica.data.ApiRoutes
import com.davidbragadeveloper.filmica.data.Film

object DiscoverFilmsRepo : BaseFilmsRepo() {

    var lastPage = 1;

    fun discoverFilms(context: Context,
                      page: Int = 1,
                      callbackSucces: ((MutableList<Film>)->Unit),
                      callbackError: ((VolleyError)->Unit)){
        if(films.isEmpty() || page > lastPage) {
            requestDiscoverFilms(
                page,
                callbackSucces,
                callbackError,
                context
            )
        }else {
            callbackSucces.invoke(films)
        }

        lastPage = page

    }

    private fun requestDiscoverFilms(
        page: Int = 1,
        callbackSucces: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        context: Context
    ) {

        val url = ApiRoutes.discoverURL(page = page)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                films.addAll(
                    Film.parseFilms(
                        it
                    )
                )
                callbackSucces.invoke(films)
            },
            {
                callbackError.invoke(it)
            })

        Volley.newRequestQueue(context).add(request)

    }

    override fun findFilmById(context: Context, id: String, callbackSuccess: (Film) -> Unit) {
        val film = films.find { it.id==id }
        film?.let { callbackSuccess.invoke(it) }
    }
}