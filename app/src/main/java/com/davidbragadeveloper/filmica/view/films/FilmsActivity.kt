package com.davidbragadeveloper.filmica.view.films


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.view.details.DetailsActivity
import com.davidbragadeveloper.filmica.view.details.DetailsFragment
import com.davidbragadeveloper.filmica.view.search.SearchFragment
import com.davidbragadeveloper.filmica.view.trendinglist.TrendingFragment
import com.davidbragadeveloper.filmica.view.utils.addFragmentsToContainer
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import com.davidbragadeveloper.filmica.view.utils.hideFragments
import com.davidbragadeveloper.filmica.view.watchlist.WatchListFragment
import kotlinx.android.synthetic.main.activity_films.*


const val FILMS_TAG = "films"
const val WATCHLIST_TAG = "watchlist"
const val TRENDING_TAG = "trendingList"
const val SEARCH_TAG = "search"

class FilmsActivity : AppCompatActivity(),
    BaseGridFilmsFragment.OnItemClickListener {

    private var fragments : MutableMap<String, Fragment> = mutableMapOf()
    private lateinit var activeFragement : Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState == null) {
            setUpFragments()
        }else{
            val activeTag = savedInstanceState.getString("active", FILMS_TAG)
            restoreFragments(activeTag)
        }

        navigation?.setOnNavigationItemSelectedListener {
            val id = it.itemId
            when (id) {
                R.id.action_discover -> showMainFragment(fragments[FILMS_TAG]!!)
                R.id.action_watchlist -> showMainFragment(fragments[WATCHLIST_TAG]!!)
                R.id.action_trendinglist -> showMainFragment(fragments[TRENDING_TAG]!!)
                R.id.action_search -> showMainFragment(fragments[SEARCH_TAG]!!)

                }
            true
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("active", activeFragement.tag)
    }


    private fun setUpFragments() {
        fragments= mutableMapOf(
            FILMS_TAG to FilmsFragment(),
            WATCHLIST_TAG to WatchListFragment(),
            TRENDING_TAG to TrendingFragment(),
            SEARCH_TAG to SearchFragment()
        )

        supportFragmentManager.beginTransaction()
            .addFragmentsToContainer(R.id.container_list, fragments)
            .hideFragments(fragments.filter {it.key != FILMS_TAG}.values.toList())
            .commit()

        activeFragement = fragments[FILMS_TAG]!!
    }

    private fun restoreFragments(activeFragmentTag: String) {
        fragments[FILMS_TAG]=supportFragmentManager.findFragmentByTag(FILMS_TAG) as FilmsFragment
        fragments[WATCHLIST_TAG] =  supportFragmentManager.findFragmentByTag(WATCHLIST_TAG) as WatchListFragment
        fragments[TRENDING_TAG] = supportFragmentManager.findFragmentByTag(TRENDING_TAG) as TrendingFragment
        fragments[SEARCH_TAG] = supportFragmentManager.findFragmentByTag(SEARCH_TAG) as SearchFragment

        activeFragement = fragments[activeFragmentTag]!!

    }

    override fun onItemClicked(film: Film) {
        showDetails(film.id)

    }

    fun showDetails(id: String) {
        Log.d("isTablet", isTablet().toString())
        if (isTablet()) {
            showDetailsFragment(id)
        }else {
            launchDetailsActivity(id)
        }

        }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(activeFragement)
            .show(fragment)
            .commit()

        activeFragement = fragment

    }

    private fun isTablet() = this.containerDetails != null


    private fun launchDetailsActivity(id: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun showDetailsFragment(id: String) {
         val detailsFragment = DetailsFragment.newInstance(id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerDetails, detailsFragment)
            .commit()
    }

}