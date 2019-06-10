package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.boba_menu_header).text =
            BobaDataManager.instance.dataManager.returnCurrentBobaStop()!!.name +
                " Menu"

        val curStore = BobaDataManager.instance.dataManager.currentBobaStop
        Log.d("BobaMenu", curStore)
        val menu = BobaDataManager.instance.dataManager.returnBobaStopMenu(curStore)!!.drinkMenu
        Log.d("BobaMenu", menu.toString())
        var menuItems = arrayListOf<String>()

        for (drink in menu) {
            menuItems.add(drink.name)
        }

        val adapter = DrinkAdapter(menuItems)
        Log.d("BobaMenu", myRecyclerView.toString())
        myRecyclerView.adapter = adapter
        myRecyclerView.setHasFixedSize(true)

        Log.d("BobaMenu", menuItems.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }
}
