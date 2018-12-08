package com.davidbragadeveloper.filmica.view.watchlist


import android.arch.lifecycle.ReportFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.FilmsRepo
import com.davidbragadeveloper.filmica.view.utils.SwipeToDeleteCallback

import kotlinx.android.synthetic.main.fragment_watch_list.*

class WatchListFragment : Fragment() {

    val adapter : WatchListAdapter by lazy {
        WatchListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSwipeHandler()
        watchList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        FilmsRepo.watchList(context!!){
            adapter.setFilms(it.toMutableList())
        }
    }

    private fun setUpSwipeHandler(){
        val swipeHandler = object: SwipeToDeleteCallback() {
            override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
                deleteFilmAt(holder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)

        itemTouchHelper.attachToRecyclerView(watchList)
    }

    private fun deleteFilmAt(position: Int) {
        val film = adapter.getFilm(position)
        FilmsRepo.deleteFilm(context!!,film){
            adapter.removeFilmAt(position)
        }

    }


}
