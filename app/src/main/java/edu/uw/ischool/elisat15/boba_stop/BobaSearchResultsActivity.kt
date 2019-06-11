package edu.uw.ischool.elisat15.boba_stop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView

class BobaSearchResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boba_search_results)
        val queryHeader = findViewById<TextView>(R.id.search_query)
        queryHeader.text = intent.getStringExtra("query")
        val searchRecyclerView = findViewById<RecyclerView>(R.id.bobaSearchResults)

        val query = intent.getStringExtra("query")
        val results = BobaDataManager.instance.dataManager.generateSearchResults(query)

        val noResults = findViewById<TextView>(R.id.no_results)
        if (results.size == 0) {
            noResults.visibility = VISIBLE
        } else {
            noResults.visibility = INVISIBLE
        }
        searchRecyclerView.adapter = SearchResultAdapter(this, results)
        searchRecyclerView.setHasFixedSize(true)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
    }

}
