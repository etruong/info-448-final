package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.location.Location
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.store_list_item.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class SearchResultAdapter(private val mContext: Context,
                          private var listOfResults: List<BobaDistance>):
    RecyclerView.Adapter<SearchResultAdapter.StoreViewHolder>() {

    private lateinit var lastLocation: Location

    override fun onCreateViewHolder(parent: ViewGroup, viewHolderType: Int): StoreViewHolder {

        val itemLayoutView = LayoutInflater.from(parent.context).inflate(R.layout.search_result_list_item, parent, false)
        return StoreViewHolder(itemLayoutView)
    }

    override fun getItemCount(): Int {
        return listOfResults.size
    }

    override fun onBindViewHolder(storeViewHolder: StoreViewHolder, position: Int) {
        val bobaStopInfo = listOfResults[position]

        storeViewHolder.bind(bobaStopInfo.name + " [${bobaStopInfo.city}]",
            bobaStopInfo.id, bobaStopInfo.distance)
    }

    inner class StoreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(storeName: String, storeId: String, distance: Double) {
            val miles = "$distance mi"
            itemView.storeName.text = storeName
            itemView.distance.text = miles

            itemView.setOnClickListener {
                val intent = Intent(mContext, BobaActivity::class.java)
                intent.putExtra("bobaStop", storeId)
                mContext.startActivity(intent)
            }
        }
    }

}