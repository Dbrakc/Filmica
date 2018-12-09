package com.davidbragadeveloper.filmica.view.films

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.DiscoverFilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import com.davidbragadeveloper.filmica.view.utils.makeInvisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_notify_joker.*


class FilmsFragment : BaseGridFilmsFragment(DiscoverFilmsRepo.films){


    override fun reload() {
        DiscoverFilmsRepo.discoverFilms(context!!,onSuccess(),onError())
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

