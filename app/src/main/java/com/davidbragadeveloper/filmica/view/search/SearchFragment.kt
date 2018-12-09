package com.davidbragadeveloper.filmica.view.search


import android.app.ActionBar
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.SearchFilmsRepo
import com.davidbragadeveloper.filmica.view.films.FilmsActivity
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import com.davidbragadeveloper.filmica.view.utils.makeInvisible
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_notify_joker.*


class SearchFragment : BaseGridFilmsFragment (SearchFilmsRepo.films), FilmsActivity.OnQueryTextChangeListener {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLayoutNoResults()
    }

    override fun reload(){}

    override fun onSuccess(): (MutableList<Film>) -> Unit {  return {
        this@SearchFragment.view?.makeInvisible(listOf(progress!!,layoutError,notifyJockerLayout))
        list.visibility = View.VISIBLE
        adapter.setFilms(it)
    } }

    override fun onError(): (VolleyError) -> Unit {
        return {
            this@SearchFragment.view?.makeInvisible(mutableListOf(progress!!, list,notifyJockerLayout))
            layoutError?.visibility = View.VISIBLE
            it.printStackTrace()
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if(it.length>=3) {
                SearchFilmsRepo.searchedFilms(query = it,
                    context = context!!,
                    callbackSucces = onSuccess(),
                    callbackNoResults = {
                        showLayoutNoResults()
                    },
                    callbackError = onError())
            }
        }
        return true
    }

    private fun showLayoutNoResults() {
        view?.makeInvisible(listOf(progress!!, list, layoutError))
        notifyJockerLabel.text = getString(R.string.no_results_label)
        notifyJockerLayout.visibility = View.VISIBLE
    }


}
