package com.davidbragadeveloper.filmica.view.watchlist


import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.util.Log
import android.view.View
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.ApiConstants
import com.davidbragadeveloper.filmica.data.BASE_POSTER_URL
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.view.utils.base.BaseFilmAdapter
import com.davidbragadeveloper.filmica.view.utils.base.BaseFilmHolder
import com.davidbragadeveloper.filmica.view.utils.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_watch_list.view.*


class WatchListAdapter(itemClickListener: ((Film)->Unit)? = null):
    BaseFilmAdapter<WatchListAdapter.WatchListHolder>(
        layoutItem = R.layout.item_watch_list,
        holderCreator = {view -> WatchListHolder(view, itemClickListener)}) {

    class WatchListHolder (itemView: View, itemClickListener: ((Film) -> Unit)?) : BaseFilmHolder(itemView, itemClickListener){


        override fun bindFilm(film: Film) {
            super.bindFilm(film)

            with(itemView){
                labelTitle.text = film.title
                labelOverview.text = film.overview
                labelVotes.text = film.voteRating.toString()
                loadImage(film)
            }
        }

        private fun loadImage(film: Film) {
            val target = SimpleTarget (successCallback = {bitmap, from ->

                itemView.imgPoster.setImageBitmap(bitmap)
                setColorFrom(bitmap)


            })
            itemView.imgPoster.tag = target


            Picasso.get()
                .load("${film.getBasePosterURL()}${film.poster}")
                .placeholder(R.drawable.placeholder)
                .into(target)

        }

        private fun setColorFrom(bitmap: Bitmap) {
            Palette.from(bitmap).generate {
                val defaultColor = ContextCompat.getColor(itemView.context!!, R.color.colorPrimary)
                val swatch = it?.vibrantSwatch ?: it?.dominantSwatch
                val color = swatch?.rgb ?: defaultColor
                val overlayColor = Color.argb(
                    (Color.alpha(color) * 0.5).toInt(),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color)
                )
                itemView.imgOverlay.setBackgroundColor(overlayColor)

            }
        }

    }
}