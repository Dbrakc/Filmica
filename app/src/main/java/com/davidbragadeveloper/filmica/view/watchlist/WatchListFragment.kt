package com.davidbragadeveloper.filmica.view.watchlist


import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.repos.WatchListRepo
import com.davidbragadeveloper.filmica.view.utils.SwipeToDeleteCallback
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment


import kotlinx.android.synthetic.main.fragment_watch_list.*

import kotlinx.android.synthetic.main.layout_notify_joker.*

class WatchListFragment : Fragment() {

    lateinit var listener: BaseGridFilmsFragment.OnItemClickListener

    val adapter : WatchListAdapter by lazy {
        WatchListAdapter{
            listener.onItemClicked(it)
        }
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
        WatchListRepo.watchList(context = context!!,
            callbackSuccess = {
                watchList.visibility = View.VISIBLE
                notifyJockerLayout.visibility = View.INVISIBLE
                adapter.setFilms(it.toMutableList())
            },
            callbackNoResults = {
                watchList.visibility = View.INVISIBLE
                notifyJockerLayout.visibility = View.VISIBLE

                notifyJockerLabel.text = getString(R.string.no_films_saved)
                notifyJockerLayout.visibility = View.VISIBLE
            }
        )
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is BaseGridFilmsFragment.OnItemClickListener){
            listener=context
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
        val context = context !!
        WatchListRepo.deleteFilm(context,film){
            adapter.removeFilmAt(position)
            Snackbar.make(this@WatchListFragment.view!!,R.string.label_delete, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo_delete)){
                    WatchListRepo.saveFilm(context,film){
                        adapter.addFilm(film)
                        Toast.makeText(context,getString(R.string.film_not_deleted), Toast.LENGTH_SHORT).show()
                    }
                }
                .show()
        }

    }


}
