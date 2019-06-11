package edu.uw.ischool.elisat15.boba_stop

import android.app.Notification.DEFAULT_SOUND
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import kotlin.math.roundToInt
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.location.Location
import android.os.Build
import android.provider.Settings.Global.getString
import android.support.v4.content.ContextCompat.getSystemService
import com.google.android.gms.maps.model.LatLng


class OnlineBobaRepository: BobaRepository {

    private val TAG: String = "OnlineBobaRepository"

    override var online: Boolean = true
    override val bobaData: ArrayList<BobaStopInfo> = arrayListOf()
    override lateinit var currentLocation: String
    override lateinit var currentBobaStop: String
    override lateinit var serviceIntent: Intent
    override var lastLocation: LatLng? = null

    // variables about new boba shop notification
    var firstInitalized: Boolean = false
    var newBobaPlace: Boolean = false
    var newBobaPlaceObject: BobaStopInfo? = null
    val CHANNEL_ID: String = "NewBobaNotification"
    val NOTIFICATION_ID: Int = 1

    private lateinit var database: DatabaseReference

    override fun returnCurrentBobaStop(): BobaStopInfo? {
        return returnBobaStop(currentBobaStop)
    }

    override fun fetchData(context: Context) {
        connectOnlineDatabase(context)
    }

    fun connectOnlineDatabase(context: Context): BobaStopInfo? {

        database = FirebaseDatabase.getInstance().reference
        createNotificationChannel(context)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.v(TAG, "enter on data change")
                val data = dataSnapshot.value as ArrayList<Any>
                reformatBobaStopData(data)
                if (newBobaPlace) {

                    newBobaPlace = false
                    bobaData.add(newBobaPlaceObject!!)
                    Log.v(TAG, "hello")
                    createNotification(context)

                } else {
                    newBobaPlaceObject = null
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.v(TAG, "loadData:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
        return newBobaPlaceObject
    }

    private fun createNotification(context: Context) {

        val bobaIntent = Intent(context, BobaActivity::class.java)
        bobaIntent.putExtra("bobaStop", newBobaPlaceObject!!.id)
        val bobaPendingIntent = PendingIntent.getActivity(context, 1, bobaIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        BobaDataManager.instance.dataManager.currentBobaStop = newBobaPlaceObject!!.id
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_local_cafe)
            .setContentTitle("New Boba Shop Opened!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Check out ${newBobaPlaceObject!!.name} it just opened up near you!"))
            .setDefaults(DEFAULT_SOUND)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(bobaPendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Boba Up"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun returnBobaStop(id: String): BobaStopInfo? {
        for (bobaStopIndex in 0 until bobaData.size) {
            if (bobaData[bobaStopIndex].id == id) {
                return bobaData[bobaStopIndex]
            }
        }
        return null
    }

    override fun returnBobaStopMenu(id: String): BobaMenu? {
        val bobaStopObject = returnBobaStop(id) ?: return null
        return bobaStopObject!!.menu
    }

    override fun returnRandomBoba(id: String): Drink? {
        val bobaMenu = returnBobaStopMenu(id) ?: return null
        val randomIndex = Math.random().times(bobaMenu.drinkMenu.size - 1).roundToInt()
        Log.v(TAG, bobaMenu.drinkMenu.toString())
        return bobaMenu.drinkMenu[randomIndex]
    }

    fun reformatBobaStopData(data: ArrayList<Any>) {

        for (bobaStopIndex in 0 until data.size) {
            val bobaStopObject = data[bobaStopIndex] as HashMap<String, Any>
            val bobaStopID = bobaStopObject.get("Id") as String
            val bobaStopName = bobaStopObject.get("Name") as String
            val bobaStopPhone = bobaStopObject.get("Phone").toString()
            val bobaStopAddress = bobaStopObject.get("Address") as String
            val bobaStopCity = bobaStopObject.get("City") as String
            val bobaStopState = bobaStopObject.get("State") as String
            val bobaStopZipCode = bobaStopObject.get("Zip_Code").toString()
            val bobaStopCoordinateLatitude = bobaStopObject.get("Coordinates_Latitude").toString()
            val bobaStopCoordinateLongitude = bobaStopObject.get("Coordinates_Longitude").toString()
            val bobaStopRating = bobaStopObject.get("Rating").toString()

            var bobaStopInfoObject = BobaStopInfo(bobaStopID, bobaStopName, bobaStopRating, bobaStopCoordinateLatitude,
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
                bobaStopInfoObject = BobaStopInfo(bobaStopID, bobaStopName, bobaStopRating, bobaStopCoordinateLatitude,
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

data class BobaStopInfo (val id: String,
                         val name: String,
                         val rating: String,
                         val coordinatesLatitude: String,
                         val coordinatesLongitude: String,
                         val address: String,
                         val city: String,
                         val zipCode: String,
                         val state: String,
                         val phone: String,
                         val menu: BobaMenu?)