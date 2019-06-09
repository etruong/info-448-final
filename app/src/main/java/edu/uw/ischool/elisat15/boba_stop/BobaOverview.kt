package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_boba_overview.*

class BobaOverview : Fragment() {

    val TAG: String = "BobaOverviewFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shopPhone = view.findViewById<TextView>(R.id.boba_phone)
        val shopRating = view.findViewById<TextView>(R.id.boba_rating)
        val shopFood = view.findViewById<TextView>(R.id.boba_food)

        val bobaInfo = BobaDataManager.instance.dataManager
            .returnBobaStop(BobaDataManager.instance.dataManager.currentBobaStop)
        val menuInfo = bobaInfo!!.menu

        var bobaStopPhone = bobaInfo!!.phone
        if (bobaStopPhone.length >= 10) {
            bobaStopPhone = bobaInfo!!.phone.substring(0, 1) + "-(" + bobaInfo!!.phone.substring(1, 4) + ")-" +
                    bobaInfo!!.phone.substring(4, 7) + "-" + bobaInfo!!.phone.substring(7)
        }

        shopName.text = bobaInfo!!.name
        shopAddress.text = "Address:\n${bobaInfo!!.address} ${bobaInfo!!.city}, ${bobaInfo!!.state} ${bobaInfo!!.zipCode}"
        shopPhone.text = "Phone Number:\n${bobaStopPhone}"
        shopRating.text = "Rating: ${bobaInfo!!.rating}"
        if (menuInfo != null) {
            var bobaFood = "Nope"
            if (menuInfo.food) {
                bobaFood = "Yes!!"
            }
            shopFood.text = "Food: ${bobaFood}"
        }

        val randomizerIntent = Intent(activity, RandomizerActivity::class.java)
        deciderBtn.setOnClickListener {
            randomizerIntent.putExtra("bobaStop", BobaDataManager.instance.dataManager.currentBobaStop)
            startActivity(randomizerIntent)
        }

        menuBtn.setOnClickListener {
            val newFragment = MenuFragment()

            val transaction = fragmentManager!!.beginTransaction().apply {
                replace(R.id.boba_overview_id, newFragment)
                addToBackStack(null)
            }

            transaction.commit()
        }

        shareBtn.setOnClickListener {
            val newFragment = ShareFragment()
            val transaction = fragmentManager!!.beginTransaction().apply {
                replace(R.id.boba_overview_id, newFragment)
                addToBackStack(null)
            }

            transaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boba_overview, container, false)
    }
}
