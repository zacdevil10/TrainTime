package uk.co.zac_h.traintime.ui.arrivals.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.utils.LineColorHelper

class TrainTimeAdapter(
    private val arrivals: ArrayList<ArrivalsModel>
) : RecyclerView.Adapter<TrainTimeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.next_train_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrivalsModel = arrivals[position]

        holder.apply {
            platformNameText.text = arrivalsModel.platformName
            trainTimeText.text = arrivalsModel.timeToStation.toString()

            trainIcon.setColorFilter(Color.parseColor(LineColorHelper.getColour(arrivalsModel.lineName)))

            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    R.id.action_arrivals_fragment_to_line_fragment,
                    bundleOf(
                        "lineName" to arrivalsModel.lineName,
                        "stationName" to arrivalsModel.stationName
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return arrivals.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val platformNameText: TextView = itemView.findViewById(R.id.platform_name_text)
        val trainTimeText: TextView = itemView.findViewById(R.id.train_time_text)
        val trainIcon: ImageView = itemView.findViewById(R.id.next_train_image)
    }
}