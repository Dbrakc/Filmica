package com.davidbragadeveloper.filmica.data.repos

import android.arch.persistence.room.Room
import android.content.Context
import com.davidbragadeveloper.filmica.data.db.AppDatabase
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.view.films.FILMS_TAG
import com.davidbragadeveloper.filmica.view.films.SEARCH_TAG
import com.davidbragadeveloper.filmica.view.films.TRENDING_TAG
import com.davidbragadeveloper.filmica.view.films.WATCHLIST_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseFilmsRepo {

    companion object {
        public fun getRepoWithStrategy(strategyTag: String): BaseFilmsRepo{
            var repo : BaseFilmsRepo? = null
            when(strategyTag){
                FILMS_TAG -> repo = DiscoverFilmsRepo
                SEARCH_TAG -> repo = SearchFilmsRepo
                TRENDING_TAG -> repo =  TrendingFilmsRepo
                WATCHLIST_TAG -> repo = WatchListRepo
            }
            return repo ?: DiscoverFilmsRepo
        }
    }

    val films : MutableList<Film>  = mutableListOf()

    @Volatile
    private var db : AppDatabase? = null

    protected fun getDbInstance (context: Context): AppDatabase {
        if(db ==null) {
           db = Room.databaseBuilder(context, AppDatabase::class.java, "filmica-db").build()
        }

        return db as AppDatabase
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


    abstract fun findFilmById(
        context: Context,
        id: String,
        callbackSuccess: (Film) -> Unit
    )



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

    fun deleteFilm (
        context: Context,
        film: Film,
        callbackSuccess: (Film) -> Unit
    ){
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO){
                WatchListRepo.getDbInstance(context).filmDao().deleteFilm(film)
            }
            async.await()
            callbackSuccess.invoke(film)

        }
    }





}