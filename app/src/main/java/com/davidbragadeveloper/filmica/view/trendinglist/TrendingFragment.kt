package com.davidbragadeveloper.filmica.view.trendinglist
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.FilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment


class TrendingFragment : BaseGridFilmsFragment(FilmsRepo.trendingFilms){
    override fun reload() {
        FilmsRepo.trendingFilms(context!!, onSuccess(),onError())
    }
}
