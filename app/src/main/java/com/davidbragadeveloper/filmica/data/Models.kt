package com.davidbragadeveloper.filmica.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

@Entity
data class Film (@PrimaryKey val id: String = UUID.randomUUID().toString(),
                 var title: String = "",
                 var genre: String = "",
                 var release: String = "",
                 @ColumnInfo (name= "vote_rating") var voteRating: Double = 0.0,
                 var overview: String = "",
                 var poster: String = ""
){

    @Ignore
    constructor(): this( "")

    fun getPosterURL():String{
        return "$BASE_POSTER_URL$poster"
    }

    companion object {
        fun parseFilms (response: JSONObject) : MutableList<Film> {
            val films = mutableListOf<Film>()
            val filmsArray = response.getJSONArray("results")

            for ( i in 0..(filmsArray.length()-1)) {
                films.add(parseFilm(filmsArray.getJSONObject(i)))
            }

            return films
        }

        fun parseFilm(jsonFilm: JSONObject): Film {

            return Film(
                id = jsonFilm.getInt("id").toString(),
                title = jsonFilm.getString("title"),
                overview = jsonFilm.getString("overview"),
                voteRating = jsonFilm.getDouble("vote_average"),
                release = jsonFilm.getString("release_date"),
                poster =  jsonFilm.optString("poster_path", ""),
                genre = parseGenres(jsonFilm.getJSONArray("genre_ids"))

            )

        }

        private fun parseGenres ( genresArray: JSONArray): String {
            val genres = mutableListOf<String>()
            for (i in 0..(genresArray.length()-1)){
                val genreId = genresArray.get(i)
                val genre = ApiConstants.genres[genreId] ?: ""
                genres.add(genre)
            }

            return genres.reduce{ acc, genre ->  "$acc | $genre"  }
        }
    }


}