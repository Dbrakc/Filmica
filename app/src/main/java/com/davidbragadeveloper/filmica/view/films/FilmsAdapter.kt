package com.davidbragadeveloper.filmica.view.films

import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.View
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.view.utils.base.BaseFilmAdapter
import com.davidbragadeveloper.filmica.view.utils.base.BaseFilmHolder
import com.davidbragadeveloper.filmica.view.utils.SimpleTarget

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_film.view.*


class FilmsAdapter(itemClickListener: ((Film)->Unit)? = null) : BaseFilmAdapter<FilmsAdapter.FilmsViewHolder>(
    layoutItem = R.layout.item_film ,
    holderCreator = { view -> FilmsViewHolder (view, itemClickListener) }
)
{

    class FilmsViewHolder(view: View,
                                listener: ((Film)->Unit)? = null): BaseFilmHolder(view, listener){

        override fun bindFilm(film: Film) {
            super.bindFilm(film)
            with(itemView){
                titleLabel.text = film.title
                genreLabel.text = film.genre
                votesLabel.text = film.voteRating.toString()
                loadImage()
            }

        }

        private fun loadImage() {
            val target = SimpleTarget (successCallback = {bitmap, from ->
                itemView.imgPoster.setImageBitmap(bitmap)
                setColorFrom(bitmap)
            })
            itemView.imgPoster.tag = target
            Picasso.get()
                .load(film?.getPosterURL())
                .placeholder(R.drawable.placeholder)
                .into(target)
        }

        private fun setColorFrom(bitmap: Bitmap) {
            Palette.from(bitmap).generate {
                val defaultColor = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                val swatch = it?.vibrantSwatch ?: it?.dominantSwatch
                val color = swatch?.rgb ?: defaultColor
                itemView.container.setBackgroundColor(color)
                itemView.dataContainer.setBackgroundColor(color)
            }
        }


    }
}