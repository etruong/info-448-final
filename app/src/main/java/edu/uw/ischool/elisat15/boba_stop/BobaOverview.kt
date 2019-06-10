package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_boba_overview.*
import android.content.BroadcastReceiver



class BobaOverview : Fragment() {

    val TAG: String = "BobaOverviewFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shopName = view.findViewById<TextView>(R.id.shopName)
        val shopPhone = view.findViewById<TextView>(R.id.boba_phone)
        val shopRating = view.findViewById<TextView>(R.id.boba_rating)
        val shopFood = view.findViewById<TextView>(R.id.boba_food)

        val bobaInfo = BobaDataManager.instance.dataManager
            .returnBobaStop(arguments!!.getString("bobaStop"))
        val menuInfo = bobaInfo!!.menu

        var bobaStopPhone = bobaInfo.phone
        if (bobaStopPhone.length >= 10) {
            bobaStopPhone = bobaInfo.phone.substring(0, 1) + "-(" + bobaInfo.phone.substring(1, 4) + ")-" +
                    bobaInfo.phone.substring(4, 7) + "-" + bobaInfo.phone.substring(7)
        }
        val bobaAddress = "${bobaInfo.address} ${bobaInfo.city}, ${bobaInfo.state} ${bobaInfo.zipCode}"

        shopName.text = bobaInfo.name
        shopAddress.text = "Address:\n${bobaAddress}"
        shopPhone.text = "Phone Number:\n${bobaStopPhone}"
        shopRating.text = "Rating: ${bobaInfo.rating}"
        if (menuInfo != null) {
            var bobaFood = "Nope"
            if (menuInfo.food) {
                bobaFood = "Yes!!"
            }
            shopFood.text = "Food: ${bobaFood}"
        } else {
            menuBtn.visibility = View.GONE
            deciderBtn.visibility = View.GONE
        }

        val randomizerIntent = Intent(activity, RandomizerActivity::class.java)
        deciderBtn.setOnClickListener {
            BobaDataManager.instance.dataManager.currentBobaStop = arguments!!.getString("bobaStop")
            randomizerIntent.putExtra("bobaStop", arguments!!.getString("bobaStop"))
            startActivity(randomizerIntent)
        }

        val menuBundle = Bundle()
        menuBundle.putString("bobaStop", arguments!!.getString("bobaStop"))
        menuBtn.setOnClickListener {
            val newFragment = MenuFragment()
            newFragment.arguments = menuBundle
            val transaction = fragmentManager!!.beginTransaction().apply {
                replace(R.id.boba_overview_id, newFragment)
                addToBackStack(null)
            }
            transaction.commit()
        }

        shareBtn.setOnClickListener {
            val bobaName = BobaDataManager.instance.dataManager.returnCurrentBobaStop()!!.name
            val addy = BobaDataManager.instance.dataManager.returnCurrentBobaStop()!!.address
            val name = bobaName.replace(" ", "%20")
            val shareAddy = addy.replace(" ", "%20")
            val shareLink = "https://www.yelp.com/search?find_desc={bobaStop}&find_loc={addy}".replace(
                "{bobaStop}",
                name
            ).replace("{addy}", shareAddy)

            val shareMsg = "Check out $bobaName!! \nIt's located at $bobaAddress! \nHere's more information: $shareLink"

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
}
