package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

const val TAG: String = "BobaRepository"

class BobaRepository {

    val bobaData: ArrayList<BobaStop> = arrayListOf()
    lateinit var currentLocation: String

    fun updateData(context: Context) {
        val jsonString: String? = try {
            // grab file from assets folder & read it to a String
            val inputStream = context.assets.open("boba-data.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            null
        }

        reformatData(jsonString!!)

    }

    fun reformatData(data: String) {
        val dataJSONArray = JSONArray(data)
        for (bobaStop in 0 until dataJSONArray.length()) {

            val bobaStopJSONObject = dataJSONArray.get(bobaStop) as JSONObject

            val bobaID = bobaStopJSONObject.getString("Id")
            val bobaName = bobaStopJSONObject.getString("Name")
            val bobaRating = bobaStopJSONObject.getString("Rating")
            val bobaCoordinateLatitude = bobaStopJSONObject.getString("Coordinates_Latitude")
            val bobaCoordinateLongitude = bobaStopJSONObject.getString("Coordinates_Longitude")
            val bobaAddress = bobaStopJSONObject.getString("Address")
            val bobaCity = bobaStopJSONObject.getString("City")
            val bobaZipCode = bobaStopJSONObject.getString("Zip_Code")
            val bobaState = bobaStopJSONObject.getString("State")
            val bobaPhone = bobaStopJSONObject.getString("Phone")

            val oneBobaStop = BobaStop(bobaID, bobaName, bobaRating, bobaCoordinateLatitude, bobaCoordinateLongitude,
                bobaAddress, bobaCity, bobaZipCode, bobaState, bobaPhone)

            bobaData.add(oneBobaStop)
        }
    }

}

data class BobaStop (val id: String, val name: String, val rating: String, val coordinatesLatitude: String,
                 val coordinatesLongitude: String, val address: String, val city: String, val zipCode: String,
                 val state: String, val phone: String)