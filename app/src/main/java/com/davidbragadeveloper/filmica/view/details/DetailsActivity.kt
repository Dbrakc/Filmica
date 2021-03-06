package com.davidbragadeveloper.filmica.view.details

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.davidbragadeveloper.filmica.R


class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val id = intent.getStringExtra("id")
        val strategy = intent.getStringExtra("strategy")

        if(savedInstanceState == null) {
            val detailsFragment = DetailsFragment.newInstance(id,strategy)
            supportFragmentManager.beginTransaction()
                .add(R.id.containerDetails, detailsFragment)
                .commit()
        }
    }

}
