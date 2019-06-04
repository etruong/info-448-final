package edu.uw.ischool.elisat15.boba_stop

import android.util.Log
import com.google.firebase.database.*

class OnlineBobaRepository {

    val bobaData: ArrayList<BobaStop> = arrayListOf()
    lateinit var currentLocation: String
    lateinit var currentBobaStop: String

    private lateinit var database: DatabaseReference

    fun connectOnlineDatabase() {

        database = FirebaseDatabase.getInstance().reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.value
//                Log.v(TAG, )

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        database.addValueEventListener(postListener)

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
                         val menu: BobaMenu)