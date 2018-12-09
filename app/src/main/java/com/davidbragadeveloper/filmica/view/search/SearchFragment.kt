package com.davidbragadeveloper.filmica.view.search


import android.os.Bundle
import android.view.View
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.SearchFilmsRepo
import com.davidbragadeveloper.filmica.view.films.FilmsActivity
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import com.davidbragadeveloper.filmica.view.utils.extensions.makeInvisible
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_notify_joker.*


class SearchFragment : BaseGridFilmsFragment (SearchFilmsRepo.films), FilmsActivity.OnQueryTextChangeListener {

    lateinit var currentQuery : String

    override fun loadPage(page: Int) {
        SearchFilmsRepo.searchedFilms(query = currentQuery,
            context = context!!,
            page = page,
            callbackSucces = onSuccess(),
            callbackNoResults = {
                showLayoutNoResults()
            },
            callbackError = onError())
    }



    override fun reload(){}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLayoutNoResults()
    }


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
            currentQuery = it
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
