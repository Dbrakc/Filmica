package com.davidbragadeveloper.filmica.view.trendinglist
import android.view.View
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.TrendingFilmsRepo
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import com.davidbragadeveloper.filmica.view.utils.extensions.makeInvisible
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_notify_joker.*


class TrendingFragment : BaseGridFilmsFragment(TrendingFilmsRepo.films){

    override fun loadPage(page: Int) {
        TrendingFilmsRepo.trendingFilms(context!!, onSuccess(),onError()){
            this@TrendingFragment.view?.makeInvisible(listOf(progress!!, list, layoutError))
            notifyJockerLabel.text = getString(R.string.no_trending_films)
            notifyJockerLayout.visibility = View.VISIBLE
        }
    }


    override fun onSuccess(): (MutableList<Film>) -> Unit {
        return {
            this@TrendingFragment.view?.makeInvisible(listOf(progress!!,layoutError!!))
            list.visibility = View.VISIBLE
            adapter.setFilms(it)
        }
    }

    override fun onError(): (VolleyError) -> Unit {
        return {
            this@TrendingFragment.view?.makeInvisible(listOf(progress!!,list))
            layoutError?.visibility = View.VISIBLE
            it.printStackTrace()
        }
    }



}
