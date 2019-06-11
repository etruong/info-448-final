package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.math.BigDecimal
import java.math.RoundingMode

interface BobaRepository {

    val bobaData: ArrayList<BobaStopInfo>
    var currentLocation: String
    var currentBobaStop: String
    var online: Boolean
    var serviceIntent: Intent
    var lastLocation: LatLng?

    fun fetchData(context: Context)
    fun returnCurrentBobaStop(): BobaStopInfo?
    fun returnBobaStop(id: String): BobaStopInfo?
    fun returnBobaStopMenu(id: String): BobaMenu?
    fun returnRandomBoba(id: String): Drink?

    fun generateSearchResults(query: String): List<BobaDistance> {
        val searchResults = mutableListOf<BobaDistance>()
        for (bobaStop in bobaData) {
            if (bobaStop.name.toLowerCase().contains("${query.toLowerCase()}")) {
                val distance = computeDistance(lastLocation!!,
                    LatLng(bobaStop.coordinatesLatitude.toDouble(), bobaStop.coordinatesLongitude.toDouble())
                )
                searchResults.add(BobaDistance(bobaStop.name, bobaStop.id, bobaStop.city, distance))
            }
        }
        var sortedSearchResults = searchResults.sortedBy {it.distance}
        return sortedSearchResults
    }

    private fun computeDistance(a: LatLng, b: LatLng) : Double {
        val distance = SphericalUtil.computeDistanceBetween(a, b)
        val decimalDistance = BigDecimal(distance / 1609.344).setScale(2, RoundingMode.HALF_UP)
        return decimalDistance.toDouble()
    }

}

data class BobaDistance(val name: String, val id: String, val city: String, val distance: Double)