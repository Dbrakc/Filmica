package com.davidbragadeveloper.filmica.view.trendinglist
import android.view.View
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.TrendingFilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*


class TrendingFragment : BaseGridFilmsFragment(TrendingFilmsRepo.films){


    override fun reload() {
        TrendingFilmsRepo.trendingFilms(context!!, onSuccess(),onError())
    }

    override fun onSuccess(): (MutableList<Film>) -> Unit {
        return {
            makeInvisible(listOf(progress!!,layoutError!!))
            list.visibility = View.VISIBLE
            adapter.setFilms(it)
        }
    }

    override fun onError(): (VolleyError) -> Unit {
        return {
            makeInvisible(mutableListOf(progress!!,list))
            layoutError?.visibility = View.VISIBLE
            it.printStackTrace()
        }
    }


}
