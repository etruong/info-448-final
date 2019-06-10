package edu.uw.ischool.elisat15.boba_stop

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings

@Suppress("DEPRECATION")
class BobaDataManager: Application() {

    lateinit var dataManager: BobaRepository
        private set

    companion object {
        lateinit var instance: BobaDataManager
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            dataManager = OnlineBobaRepository()
        } else {
            dataManager = OfflineBobaRepository()
        }
        dataManager.fetchData(this)
    }

}