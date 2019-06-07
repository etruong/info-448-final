package edu.uw.ischool.elisat15.boba_stop

import android.content.Context

interface BobaRepository {

    val bobaData: ArrayList<BobaStopInfo>
    var currentLocation: String
    var currentBobaStop: String

    fun fetchData(context: Context)
    fun returnBobaStop(name: String): BobaStopInfo?
    fun returnBobaStopMenu(name: String): BobaMenu?
    fun returnRandomBoba(name: String): Drink?

}