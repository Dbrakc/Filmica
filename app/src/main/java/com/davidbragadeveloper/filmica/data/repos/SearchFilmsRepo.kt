package com.davidbragadeveloper.filmica.data.repos

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.davidbragadeveloper.filmica.data.ApiRoutes
import com.davidbragadeveloper.filmica.data.Film

object SearchFilmsRepo : BaseFilmsRepo() {

    private var lastQuery = ""

    fun searchedFilms(
        context: Context,
        query: String,
        callbackSucces: (MutableList<Film>)->Unit,
        callbackNoResults: ()->Unit,
        callbackError: (VolleyError)->Unit
    ) {

        if(lastQuery == query && films.isEmpty()) {
            callbackSucces.invoke(films)
        }else {
            requestSearchedFilms(
                query,
                callbackSucces,
                callbackNoResults,
                callbackError,
                context
            )
        }
        lastQuery = query

    }

    private fun requestSearchedFilms(
        query: String,
        callbackSucces: (MutableList<Film>) -> Unit,
        callbackNoResults: () -> Unit,
        callbackError: (VolleyError) -> Unit, context: Context
    )
    {
        val url = ApiRoutes.searchedURL(query = query)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                films.clear()
                films.addAll(
                    Film.parseFilms(
                        it, 10
                    )
                )
                if(!films.isEmpty()) {
                    callbackSucces(films)
                }else{
                    callbackNoResults()
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
            callbackSuccess(it)
        }
    }
}