package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_boba_overview.*

class BobaOverview : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BobaOverview", BobaDataManager.instance.dataManager.currentBobaStop)

        val bobaInfo = BobaDataManager.instance.dataManager
            .returnBobaStop(BobaDataManager.instance.dataManager.currentBobaStop)

        shopName.text = BobaDataManager.instance.dataManager.currentBobaStop.capitalizeWords()
        Log.d("BobaOverview", bobaInfo.toString())
        shopAddress.text = bobaInfo!!.address
        shopPhone.text = bobaInfo!!.phone
        Log.d("BobaOverview", shopPhone.text.toString())

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
            val bobaName = BobaDataManager.instance.dataManager.currentBobaStop.capitalizeWords()
            val addy = BobaDataManager.instance.dataManager.returnBobaStop(bobaName)!!.address
            val name = bobaName.replace(" ", "%20")
            val shareAddy = addy.replace(" ", "%20")
            val shareLink = "https://www.yelp.com/search?find_desc={bobaStop}&find_loc={addy}".replace(
                "{bobaStop}",
                name
            ).replace("{addy}", shareAddy)

            val shareMsg = "Check out $bobaName: $shareLink"

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("sms:")
            intent.putExtra("sms_body", shareMsg)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boba_overview, container, false)
    }

    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

}
