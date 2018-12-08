package com.davidbragadeveloper.filmica.data

import android.arch.persistence.room.Room
import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object FilmsRepo{
    val films : MutableList<Film>  = mutableListOf()

    @Volatile
    private var db : AppDatabase? = null

    private fun getDbInstance (context: Context): AppDatabase{
        if(db ==null) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "filmica-db").build()
        }

        return db as AppDatabase
    }


    fun findFilmById(id: String): Film? {
        return films.find { it.id==id }
    }

    fun dummyFilms(): List<Film>{
        return (0..9).map{
            Film(
                title = "Film $it",
                genre = "Genre $it",
                release = "200$it-0$it-0$it",
                voteRating = it.toDouble(),
                overview = "Overview $it"
            )
        }
    }

    fun saveFilm (
        context: Context,
        film: Film,
        callbackSuccess: (Film) -> Unit){
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO){
                getDbInstance(context).filmDao().insertFilm(film)
            }
            async.await()
            callbackSuccess.invoke(film)
        }
    }

    fun watchList (
        context: Context,
        callbackSuccess: (List<Film>) -> Unit)
    {
        GlobalScope.launch(Dispatchers.Main){
            val async = async (Dispatchers.IO){
                getDbInstance(context).filmDao().getFilms()
            }

            val films = async.await()
            callbackSuccess.invoke(films)
        }

    }

    fun deleteFilm (
        context: Context,
        film: Film,
        callbackSuccess: (Film) -> Unit
    ){
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO){
                getDbInstance(context).filmDao().deleteFilm(film)
            }

            async.await()
            callbackSuccess.invoke(film)

        }
    }

    fun discoverFilms(context: Context, callbackSucces: ((MutableList<Film>)->Unit), callbackError: ((VolleyError)->Unit)){
        if(films.isEmpty()) {
            requestDiscoverFilms(callbackSucces, callbackError, context)
        }else {
            callbackSucces.invoke(films)
        }

    }

    private fun requestDiscoverFilms(
        callbackSucces: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        context: Context
    ) {
        val url = ApiRoutes.discoverURL()
        val request = JsonObjectRequest(Request.Method.GET, url, null,
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