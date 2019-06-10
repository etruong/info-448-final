package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val stores = BobaDataManager.instance.dataManager.bobaData
        var storeNames = arrayListOf<String>()

        for (store in stores) {
            storeNames.add(store.name)
        }
        val adapter = StoreAdapter(storeNames)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val mapButton = findViewById<Button>(R.id.mapButton)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}