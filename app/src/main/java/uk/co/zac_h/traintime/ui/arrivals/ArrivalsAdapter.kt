package uk.co.zac_h.traintime.ui.arrivals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.StationModel

class ArrivalsAdapter(private val stations: ArrayList<StationModel>): RecyclerView.Adapter<ArrivalsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.arrivals_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (id, commonName) = stations[position]

        holder.stationNameText.text = commonName
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val stationNameText: TextView = itemView.findViewById(R.id.station_name_text)
        val nextTrainRecycler: RecyclerView = itemView.findViewById(R.id.next_train_recycler)
    }
}