package edu.uw.ischool.elisat15.boba_stop

import android.app.Application

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

        dataManager = OfflineBobaRepository()
        dataManager.fetchData(this)
    }

}