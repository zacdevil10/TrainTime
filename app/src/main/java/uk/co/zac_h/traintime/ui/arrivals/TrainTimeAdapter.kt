package uk.co.zac_h.traintime.ui.arrivals

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.utils.LineColorHelper

class TrainTimeAdapter(
    private val arrivals: ArrayList<ArrivalsModel>,
    private val view: ArrivalsView
): RecyclerView.Adapter<TrainTimeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.next_train_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrivalsModel = arrivals[position]

        holder.platformNameText.text = arrivalsModel.platformName
        holder.trainTimeText.text = arrivalsModel.timeToStation.toString()

        holder.trainIcon.setColorFilter(Color.parseColor(LineColorHelper.getColour(arrivalsModel.lineName)))

        holder.bind(arrivalsModel, view)
    }

    override fun getItemCount(): Int {
        return arrivals.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val platformNameText: TextView = itemView.findViewById(R.id.platform_name_text)
        val trainTimeText: TextView = itemView.findViewById(R.id.train_time_text)
        val trainIcon: ImageView = itemView.findViewById(R.id.next_train_image)

        fun bind(arrivalsModel: ArrivalsModel, view: ArrivalsView) {
            itemView.setOnClickListener {
                view.showTrainLineFragment(arrivalsModel.lineName, arrivalsModel.stationName)
            }
        }
    }
}