package edu.uw.ischool.elisat15.boba_stop

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CheckNewBobaService: Service() {

    override fun onBind(intent: Intent?): IBinder? {return null}

    override fun onCreate() {
        super.onCreate()

    }


}