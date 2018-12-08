package com.davidbragadeveloper.filmica.view.utils

import android.widget.SearchView

class QueryTextChangeListener : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let{
            if(it.length > 3){
                //TODO: Make the Request
            }
        }
        return true
    }

}