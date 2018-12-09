package com.davidbragadeveloper.filmica.view.utils.base

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davidbragadeveloper.filmica.data.Film

open class BaseFilmAdapter<VH: BaseFilmHolder>(
    @LayoutRes val layoutItem : Int,
    val holderCreator: (View) -> VH
): RecyclerView.Adapter<VH>() {

    protected val list = mutableListOf<Film>()

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(recyclerView: ViewGroup, viewType: Int): VH {

        val itemView =
            LayoutInflater.from(recyclerView.context).inflate(layoutItem, recyclerView, false)

        return  holderCreator.invoke(itemView)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        val film = list[position]
        holder.bindFilm(film)
    }

    fun setFilms (films: MutableList<Film>){
        list.clear()
        list.addAll(films)
        notifyDataSetChanged()
    }

    fun removeFilmAt (position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addFilm(film: Film){
        list.add(film)
        notifyDataSetChanged()
    }

    fun getFilm (position: Int): Film{
        return list[position]
    }
}