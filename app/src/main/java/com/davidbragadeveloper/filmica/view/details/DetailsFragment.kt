package com.davidbragadeveloper.filmica.view.details

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.*
import android.widget.Toast
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.data.repos.*
import com.davidbragadeveloper.filmica.view.utils.SimpleTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.layout_notify_joker.*


class DetailsFragment : Fragment () {


    companion object {
        fun newInstance (id: String, strategy: String) : DetailsFragment {
            val instance = DetailsFragment()
            val args = Bundle ()
            args.putString("id", id)
            args.putSerializable("strategy", strategy)
            instance.arguments = args

            return instance
        }
    }



    private lateinit var repo: BaseFilmsRepo
    private var film: Film? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_details,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_share -> {
                shareFilm()
            }
        }

        /*item.takeIf { item.itemId == R.id.action_share }?.let {

        }*/
        return super.onOptionsItemSelected(item)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNotifyJockerLayout()
        showNotifyJockerLayout()

        val id = arguments?.getString("id") ?: ""

        repo = BaseFilmsRepo.getRepoWithStrategy(arguments?.getString("strategy", "")?:"")

        repo.findFilmById(
            context = context!!,
            id = id){film ->

            showScrollView()

            with(film) {
                titleLabel.text = title
                overviewLabel.text = overview
                genreLabel.text = genre
                releaseLabel.text = release
                loadImage(this)
            }

            setOnClickListener(film)

        }

    }

    private fun showNotifyJockerLayout() {
        notifyJockerLayout.visibility = View.VISIBLE
        scrollView.visibility = View.INVISIBLE
    }

    private fun setOnClickListener(film: Film) {
        addButton.setOnClickListener { view ->
            val context = context!!
            repo.saveFilm(context, film) { film ->
                Snackbar.make(view, R.string.label_add, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo_save)) {
                        repo.deleteFilm(context, film) {
                            Toast.makeText(context, getString(R.string.film_not_saved), Toast.LENGTH_SHORT).show()
                        }
                    }
                    .show()
            }
        }
    }


    private fun setNotifyJockerLayout() {
        loadNotifyJockerIcon()
        setNotifyJockerLabel()
        notifyJockerLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
    }


    private fun setNotifyJockerLabel() {
        notifyJockerLabel.text = getString(R.string.click_movie_details)
        notifyJockerLabel.setTextColor(Color.WHITE)
    }

    private fun loadNotifyJockerIcon() {
        Picasso.get()
            .load(R.drawable.ic_movie)
            .into(notifyJockerIcon)
    }

    //Ends TODO

    private fun loadImage(film: Film) {
        val target = SimpleTarget (successCallback = {bitmap, from ->
            imgPoster.setImageBitmap(bitmap)
            setColorFrom(bitmap)
        })
        imgPoster.tag = target
        Picasso.get()
            .load(film.getPosterURL())
            .placeholder(R.drawable.placeholder)
            .into(target)
    }

    private fun setColorFrom(bitmap: Bitmap) {
        Palette.from(bitmap).generate {
            val defaultColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
            val swatch = it?.vibrantSwatch ?: it?.dominantSwatch
            val color = swatch?.rgb ?: defaultColor
            val overlayColor = Color.argb(
                (Color.alpha(color) * 0.5).toInt(),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )
            overlay.setBackgroundColor(overlayColor)
            addButton.backgroundTintList = ColorStateList.valueOf(color)

        }
    }

    private fun shareFilm() {
        val intent = Intent(Intent.ACTION_SEND)
        film?.let {
            val template = getString(R.string.template_share, it.title, it.voteRating)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, template)
        }

        startActivity(Intent.createChooser(intent, getString(R.string.title_share)))
    }

    protected fun showScrollView(){
        notifyJockerLayout.visibility = View.INVISIBLE
        scrollView.visibility = View.VISIBLE

    }

}