package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil.computeDistanceBetween
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.store_list_item.*
import java.math.BigDecimal
import java.math.RoundingMode

class ListActivity : AppCompatActivity() {

    data class Store(val name: String, val id: String, val distance: Double)
    private fun selector(s: Store): Double = s.distance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val storeInfo = BobaDataManager.instance.dataManager.bobaData
        val stores = arrayListOf<Store>()
        val currentLat = intent.getStringExtra("currentLat").toDouble()
        val currentLng = intent.getStringExtra("currentLng").toDouble()
        val currentLocation = LatLng(currentLat, currentLng)

        for (store in storeInfo) {
            val distance = computeDistance(currentLocation, LatLng(store.coordinatesLatitude.toDouble(), store.coordinatesLongitude.toDouble()))
            val newStore = Store(store.name, store.id, distance)
            stores.add(newStore)
        }
        stores.sortBy{selector(it)}
        val adapter = StoreAdapter(this, stores)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val mapButton = findViewById<Button>(R.id.mapButton)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun computeDistance(a: LatLng, b: LatLng) : Double {
        val distance = computeDistanceBetween(a, b)
        val decimalDistance = BigDecimal(distance / 1609.344).setScale(2, RoundingMode.HALF_UP)
        return decimalDistance.toDouble()
    }
}