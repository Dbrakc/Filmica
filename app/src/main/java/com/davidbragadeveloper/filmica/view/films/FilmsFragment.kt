package com.davidbragadeveloper.filmica.view.films

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.FilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment


class FilmsFragment : BaseGridFilmsFragment(){
    override fun reload() {
        FilmsRepo.discoverFilms(context!!,onSuccess(),onError())
    }
}

