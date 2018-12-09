package com.davidbragadeveloper.filmica.view.films

import android.view.View
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.DiscoverFilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*


class FilmsFragment : BaseGridFilmsFragment(DiscoverFilmsRepo.films){
    override fun onSuccess(): (MutableList<Film>) -> Unit {
        return {
            makeInvisible(listOf(progress!!,layoutError!!))
            list.visibility = View.VISIBLE
            adapter.setFilms(it)
        }
    }


    override fun reload() {
        DiscoverFilmsRepo.discoverFilms(context!!,onSuccess(),onError())
    }

    override fun onError(): (VolleyError) -> Unit {
        return {
            makeInvisible(mutableListOf(progress!!,list))
            layoutError?.visibility = View.VISIBLE
            it.printStackTrace()
        }
    }
}

