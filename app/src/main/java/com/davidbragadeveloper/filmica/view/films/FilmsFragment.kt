package com.davidbragadeveloper.filmica.view.films

import android.view.View
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.DiscoverFilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import com.davidbragadeveloper.filmica.view.utils.extensions.makeInvisible
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*


class FilmsFragment : BaseGridFilmsFragment(DiscoverFilmsRepo.films){
    override fun loadPage(page: Int) {
        DiscoverFilmsRepo.discoverFilms(context!!, page, onSuccess(),onError())
    }


    override fun onSuccess(): (MutableList<Film>) -> Unit {
        return {
            this@FilmsFragment.view?.makeInvisible(listOf(progress!!,layoutError!!))
            list.visibility = View.VISIBLE
            adapter.setFilms(it)
        }
    }

    override fun onError(): (VolleyError) -> Unit {
        return {
            this@FilmsFragment.view?.makeInvisible(mutableListOf(progress!!,list))
            layoutError?.visibility = View.VISIBLE
            it.printStackTrace()
        }
    }
}

