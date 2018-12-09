package com.davidbragadeveloper.filmica.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.davidbragadeveloper.filmica.data.Film

@Database(entities = [Film::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao () : FilmDAO
}