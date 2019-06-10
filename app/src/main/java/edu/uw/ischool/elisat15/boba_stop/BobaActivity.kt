package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_boba_overview.*

class BobaActivity : AppCompatActivity() {

    val TAG: String = "BobaActivity"
    val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boba)

        generateHomeBobaActivity(false)
    }

    override fun onNewIntent(intent: Intent) {
        generateHomeBobaActivity(true)
    }

    private fun generateHomeBobaActivity(returnStack: Boolean) {

        val chosenBobaStop = intent.getStringExtra("bobaStop")
        BobaDataManager.instance.dataManager.currentBobaStop = chosenBobaStop
        val bundle = Bundle()
        bundle.putString("bobaStop", chosenBobaStop)

        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = BobaOverview()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.boba_activity_id, fragment)
        if (returnStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }
}
