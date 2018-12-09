package com.davidbragadeveloper.filmica.view.utils.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.VolleyError
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.view.films.FilmsAdapter
import com.davidbragadeveloper.filmica.view.utils.GridRecyclerViewScrollListener
import com.davidbragadeveloper.filmica.view.utils.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*

abstract class BaseGridFilmsFragment (
    val films: MutableList<Film>
) : Fragment() {


    val list : RecyclerView by lazy {
        val instance =view!!.findViewById<RecyclerView>(R.id.recyclerList)
        instance.addItemDecoration(ItemOffsetDecoration(R.dimen.offset_grid))
        instance.setHasFixedSize(true)
        instance.setOnScrollListener(GridRecyclerViewScrollListener(instance.layoutManager as GridLayoutManager){
            loadPage (it + 1)
        })
        instance
    }



    lateinit var listener: BaseGridFilmsFragment.OnItemClickListener

    val adapter: FilmsAdapter by lazy {
        val instance = FilmsAdapter {

            this.listener.onItemClicked(it)
        }
        instance.setFilms(films)
        instance
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is OnItemClickListener){
            listener = context
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_films, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = adapter
        retryBtn?.setOnClickListener { this.reload() }

    }

    override fun onResume() {
        super.onResume()
        this.reload()
    }

    open fun reload(){
        loadPage(1)
    }

    abstract fun loadPage(page: Int)

    abstract fun onError(): (VolleyError) -> Unit

    abstract fun onSuccess(): (MutableList<Film>) -> Unit

    interface OnItemClickListener { fun onItemClicked (film: Film) }




}