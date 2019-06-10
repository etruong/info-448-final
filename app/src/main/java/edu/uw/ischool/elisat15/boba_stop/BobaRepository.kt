package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent

interface BobaRepository {

    val bobaData: ArrayList<BobaStopInfo>
    var currentLocation: String
    var currentBobaStop: String
    var online: Boolean
    var serviceIntent: Intent

    fun fetchData(context: Context)
    fun returnCurrentBobaStop(): BobaStopInfo?
    fun returnBobaStop(id: String): BobaStopInfo?
    fun returnBobaStopMenu(id: String): BobaMenu?
    fun returnRandomBoba(id: String): Drink?

}