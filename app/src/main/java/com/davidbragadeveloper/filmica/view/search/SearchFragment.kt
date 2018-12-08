package com.davidbragadeveloper.filmica.view.search


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.FilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment


class SearchFragment : BaseGridFilmsFragment (FilmsRepo.searchedFilms) {
    override fun reload() {

    }


}
