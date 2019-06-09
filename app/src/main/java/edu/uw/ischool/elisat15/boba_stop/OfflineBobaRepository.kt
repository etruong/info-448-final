package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.math.roundToInt

const val TAG: String = "OfflineBobaRepository"

class OfflineBobaRepository : BobaRepository {

    override val bobaData: ArrayList<BobaStopInfo> = arrayListOf()
    val bobaMenuData: ArrayList<BobaMenu> = arrayListOf()
    override lateinit var currentLocation: String
    override lateinit var currentBobaStop: String

    override fun fetchData(context: Context) {
        updateData(context)
    }

    override fun returnBobaStop(name: String): BobaStopInfo? {
        val nameLower = name.toLowerCase()
        for (num in 0 until bobaData.size) {
            val bobaStop = bobaData.get(num)
            Log.d("OffRepo", nameLower + " " + bobaStop.name)

            if (bobaStop.name.toLowerCase() == nameLower) {
                return bobaStop
            }
        }
        return null
    }

    fun fetchBobaMenuData(context: Context) {
        val jsonString: String? = try {
            // grab file from assets folder & read it to a String
            val inputStream = context.assets.open("menu.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            null
        }
        reformatBobaMenuData(jsonString)
    }

    fun reformatBobaMenuData(data: String?) {
        val dataJSONArray = JSONArray(data)
        for (bobaMenuIndex in 0 until dataJSONArray.length()) {
            val dataJSONObject = dataJSONArray.get(bobaMenuIndex) as JSONObject
            val bobaStopName = dataJSONObject.getString("name")
            val bobaStopSelfServe = dataJSONObject.getBoolean("self-serve")
            val bobaStopFood = dataJSONObject.getBoolean("food")
            Log.v(TAG, dataJSONObject.toString())
            val bobaStopDrinks = arrayListOf<Drink>()
            val bobaStopDrinksJSONArray = dataJSONObject.getJSONArray("drinks")
            for (bobaDrinkIndex in 0 until bobaStopDrinksJSONArray.length()) {
                val drink = bobaStopDrinksJSONArray[bobaDrinkIndex] as JSONObject
                val drinkObject = Drink(
                    drink.getString("category"),
                    drink.getString("name"),
                    drink.getBoolean("reccommended"),
                    drink.getBoolean("hot"),
                    drink.getBoolean("non-caffeinated")
                )
                bobaStopDrinks.add(drinkObject)
            }
            val bobaMenu = BobaMenu(bobaStopName, bobaStopSelfServe, bobaStopFood, bobaStopDrinks)
            bobaMenuData.add(bobaMenu)
        }
    }

    override fun returnBobaStopMenu(name: String): BobaMenu? {
        for (index in 0 until bobaMenuData.size) {
            val bobaMenu = bobaMenuData.get(index)
            if (bobaMenu.name.toLowerCase() == name.toLowerCase()) {
                return bobaMenu
            }
        }
        return null
    }

    override fun returnRandomBoba(name: String): Drink? {
        val bobaMenu = returnBobaStopMenu(name)
        if (bobaMenu != null) {
            val bobaDrinks = bobaMenu!!.drinkMenu
            val randomIndex = Math.random().times(bobaDrinks.size - 1).roundToInt()
            return bobaDrinks[randomIndex]
        } else {
            return null
        }

    }

    fun updateData(context: Context) {

        fetchBobaMenuData(context)

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

        reformatBobaStopData(jsonString!!)

    }

    fun reformatBobaStopData(data: String) {
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

            val oneBobaStop = BobaStopInfo(
                bobaName, bobaRating, bobaCoordinateLatitude, bobaCoordinateLongitude,
                bobaAddress, bobaCity, bobaZipCode, bobaState, bobaPhone, returnBobaStopMenu(bobaName)
            )

            bobaData.add(oneBobaStop)
        }
    }
}

data class BobaStop(
    val id: String, val name: String, val rating: String, val coordinatesLatitude: String,
    val coordinatesLongitude: String, val address: String, val city: String, val zipCode: String,
    val state: String, val phone: String
)

data class BobaMenu(val name: String, val selfServe: Boolean, val food: Boolean, val drinkMenu: ArrayList<Drink>)

data class Drink(
    val category: String, val name: String, val reccommended: Boolean, val hot: Boolean,
    val nonCaffeinated: Boolean
)