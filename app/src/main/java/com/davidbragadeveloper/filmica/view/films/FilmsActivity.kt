package com.davidbragadeveloper.filmica.view.films


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import com.davidbragadeveloper.filmica.R
import com.davidbragadeveloper.filmica.data.Film
import com.davidbragadeveloper.filmica.view.details.DetailsActivity
import com.davidbragadeveloper.filmica.view.details.DetailsFragment
import com.davidbragadeveloper.filmica.view.search.SearchFragment
import com.davidbragadeveloper.filmica.view.trendinglist.TrendingFragment
import com.davidbragadeveloper.filmica.view.utils.extensions.addFragmentsToContainer
import com.davidbragadeveloper.filmica.view.utils.base.BaseGridFilmsFragment
import com.davidbragadeveloper.filmica.view.utils.extensions.hideFragments
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
    private var currentTag : String = FILMS_TAG
    private var menu : Menu? = null
    private lateinit var queryListener: OnQueryTextChangeListener


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
            var tag : String? = null
            when (id) {
                R.id.action_discover -> tag = FILMS_TAG
                R.id.action_watchlist -> tag = WATCHLIST_TAG
                R.id.action_trendinglist -> tag = TRENDING_TAG
                R.id.action_search -> tag = SEARCH_TAG
            }

            currentTag = tag!!
            showMainFragment(fragments[currentTag]!!)
            menu?.let{
                val menuItem = it.findItem(R.id.search_menu)
                menuItem.isVisible = currentTag == SEARCH_TAG
                val searchView = menuItem.actionView as SearchView
                searchView.setOnQueryTextListener(queryListener)
            }
            true
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        menu?.let {
            val menuItem = it.findItem(R.id.search_menu)
            menuItem.isVisible = currentTag == SEARCH_TAG
        }
        return true
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("active", activeFragement.tag)
    }

    override fun onResume() {
        super.onResume()

        if ( fragments[SEARCH_TAG] is OnQueryTextChangeListener) {
            queryListener = fragments[SEARCH_TAG] as OnQueryTextChangeListener
        }
        if(isTablet()){
            showDetails("-1")
        }

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
        fragments[FILMS_TAG]= supportFragmentManager.findFragmentByTag(FILMS_TAG) as FilmsFragment
        fragments[WATCHLIST_TAG] =  supportFragmentManager.findFragmentByTag(WATCHLIST_TAG) as WatchListFragment
        fragments[TRENDING_TAG] = supportFragmentManager.findFragmentByTag(TRENDING_TAG) as TrendingFragment
        fragments[SEARCH_TAG] = supportFragmentManager.findFragmentByTag(SEARCH_TAG) as SearchFragment


        currentTag = activeFragmentTag
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
        if(fragment is WatchListFragment){
            fragment.onResume()
        }
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
        intent.putExtra("strategy",currentTag)
        startActivity(intent)
    }

    private fun showDetailsFragment(id: String) {
         val detailsFragment = DetailsFragment.newInstance(id, currentTag)
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerDetails, detailsFragment)
            .commit()
    }

    interface OnQueryTextChangeListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean

    }

}