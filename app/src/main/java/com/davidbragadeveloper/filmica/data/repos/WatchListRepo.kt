package com.davidbragadeveloper.filmica.data.repos

import android.content.Context
import android.util.Log
import com.davidbragadeveloper.filmica.data.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object WatchListRepo: BaseFilmsRepo() {

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

    override fun findFilmById(
        context: Context,
        id: String,
        callbackSuccess: (Film) -> Unit
    ){
        GlobalScope.launch (Dispatchers.Main) {
            val async = async(Dispatchers.IO){
                getDbInstance(context).filmDao().findFilmBy(id)
            }

            val film = async.await()

            film?.let {
                callbackSuccess(it)
            }

        }

    }
}