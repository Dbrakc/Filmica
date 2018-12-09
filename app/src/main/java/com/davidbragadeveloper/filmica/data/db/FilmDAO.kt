package com.davidbragadeveloper.filmica.data.db

import android.arch.persistence.room.*
import com.davidbragadeveloper.filmica.data.Film

@Dao
interface FilmDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilm(film: Film)

    @Query("SELECT * FROM film")
    fun getFilms():List<Film>

    @Query("SELECT * FROM film  WHERE id=:id")
    fun findFilmBy(id: String): Film?

    @Delete
    fun deleteFilm(film: Film)
}