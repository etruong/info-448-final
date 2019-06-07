package edu.uw.ischool.elisat15.boba_stop

import android.app.Notification.DEFAULT_SOUND
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import kotlin.math.roundToInt

class OnlineBobaRepository: BobaRepository {

    override fun fetchData(context: Context) {
        connectOnlineDatabase(context)
    }

    private val TAG: String = "OnlineBobaRepository"

    override val bobaData: ArrayList<BobaStopInfo> = arrayListOf()
    override lateinit var currentLocation: String
    override lateinit var currentBobaStop: String

    // variables about new boba shop notification
    var firstInitalized: Boolean = false
    var newBobaPlace: Boolean = false
    var newBobaPlaceObject: BobaStopInfo? = null
    val CHANNEL_ID: String = "NewBobaNotification"
    val NOTIFICATION_ID: Int = 1

    private lateinit var database: DatabaseReference

    fun connectOnlineDatabase(context: Context): BobaStopInfo? {

        database = FirebaseDatabase.getInstance().reference
        Log.v(TAG, "database variable: ${database.toString()}")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.v(TAG, "enter on data change")
                val data = dataSnapshot.value as ArrayList<Any>
                reformatBobaStopData(data)
                if (newBobaPlace) {

                    newBobaPlace = false
                    Log.v(TAG, "hello")
                    createNotification(context)

                } else {
                    newBobaPlaceObject = null
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadData:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)

        return newBobaPlaceObject

    }

    private fun createNotification(context: Context) {
        val bobaIntent = Intent(context, BobaActivity::class.java)
        val bobaPendingIntent = PendingIntent.getActivity(context, 1, bobaIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        BobaDataManager.instance.dataManager.currentBobaStop = newBobaPlaceObject!!.name
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_local_cafe)
            .setContentTitle("New Boba Shop Opened!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Check out ${newBobaPlaceObject!!.name} it just opened up near you!"))
            .setDefaults(DEFAULT_SOUND)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(bobaPendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    override fun returnBobaStop(name: String): BobaStopInfo? {
        for (bobaStopIndex in 0 until bobaData.size) {
            if (bobaData[bobaStopIndex].name == name) {
                return bobaData[bobaStopIndex]
            }
        }
        return null
    }

    override fun returnBobaStopMenu(name: String): BobaMenu? {
        val bobaStopObject = returnBobaStop(name) ?: return null
        return bobaStopObject!!.menu
    }

    override fun returnRandomBoba(name: String): Drink? {
        val bobaMenu = returnBobaStopMenu(name) ?: return null
        val randomIndex = Math.random().times(bobaMenu.drinkMenu.size - 1).roundToInt()
        Log.v(TAG, bobaMenu.drinkMenu.toString())
        return bobaMenu.drinkMenu[randomIndex]
    }

    fun reformatBobaStopData(data: ArrayList<Any>) {

        for (bobaStopIndex in 0 until data.size) {
            val bobaStopObject = data[bobaStopIndex] as HashMap<String, Any>
            val bobaStopName = bobaStopObject.get("Name") as String
            val bobaStopPhone = bobaStopObject.get("Phone").toString()
            val bobaStopAddress = bobaStopObject.get("Address") as String
            val bobaStopCity = bobaStopObject.get("City") as String
            val bobaStopState = bobaStopObject.get("State") as String
            val bobaStopZipCode = bobaStopObject.get("Zip_Code").toString()
            val bobaStopCoordinateLatitude = bobaStopObject.get("Coordinates_Latitude").toString()
            val bobaStopCoordinateLongitude = bobaStopObject.get("Coordinates_Longitude").toString()
            val bobaStopRating = bobaStopObject.get("Rating").toString()

            var bobaStopInfoObject = BobaStopInfo(bobaStopName, bobaStopRating, bobaStopCoordinateLatitude,
                bobaStopCoordinateLongitude, bobaStopAddress, bobaStopCity, bobaStopZipCode, bobaStopState,
                bobaStopPhone, null)

            if (bobaStopObject.containsKey("Detail")) {

                Log.v(TAG, "${bobaStopName} contains more details!!")

                val bobaStopDetail = bobaStopObject.get("Detail") as HashMap<String, Any>
                val bobaStopSelfServe = bobaStopDetail.get("Self-serve").toString().toBoolean()
                val bobaStopFood = bobaStopDetail.get("Food").toString().toBoolean()
                val bobaStopDrinks = bobaStopDetail.get("Drinks") as ArrayList<Any>

                val arrayListDrinks = arrayListOf<Drink>()

                for (bobaStopDrinksIndex in 0 until bobaStopDrinks.size) {
                    val drinks = bobaStopDrinks[bobaStopDrinksIndex] as HashMap<String, String>
                    Log.v(TAG, drinks.toString())
                    val drinkName = drinks["name"] as String
                    val drinkCategory = drinks["category"] as String
                    val drinkCaffeinated = drinks["non-caffeinated"].toString().toBoolean()
                    val drinkReccommended = drinks["reccommended"].toString().toBoolean()
                    val drinkHot = drinks["hot"].toString().toBoolean()

                    val drinkObject = Drink(drinkCategory!!, drinkName, drinkReccommended, drinkHot, drinkCaffeinated)
                    arrayListDrinks.add(drinkObject)
                }

                var bobaStopDetailObject = BobaMenu(bobaStopName, bobaStopSelfServe, bobaStopFood, arrayListDrinks)
                bobaStopInfoObject = BobaStopInfo(bobaStopName, bobaStopRating, bobaStopCoordinateLatitude,
                    bobaStopCoordinateLongitude, bobaStopAddress, bobaStopCity, bobaStopZipCode, bobaStopState,
                    bobaStopPhone, bobaStopDetailObject)

            }

            if (!bobaData.contains(bobaStopInfoObject) && firstInitalized) {
                newBobaPlace = true
                newBobaPlaceObject = bobaStopInfoObject
                Log.v(TAG, "new data: ${bobaStopName}")
            }
            bobaData.add(bobaStopInfoObject)
        }

        firstInitalized = true
    }

}

data class BobaStopInfo (val name: String,
                         val rating: String,
                         val coordinatesLatitude: String,
                         val coordinatesLongitude: String,
                         val address: String,
                         val city: String,
                         val zipCode: String,
                         val state: String,
                         val phone: String,
                         val menu: BobaMenu?)