package uk.co.zac_h.traintime.ui.lines

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.StopPointModel

class StopPointsAdapter(private val context: Context?, private val stationName: String, private val lineStopPointsArray: ArrayList<StopPointModel>): RecyclerView.Adapter<StopPointsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.line_stop_point_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stopPointModel = lineStopPointsArray[position]

        var reformatName = stopPointModel.name
        //Due to this being a tube app, we remove the text " Underground Station" from the stations that have it because we already know that :D
        if (reformatName.contains(" Underground Station")) reformatName = reformatName.replace(" Underground Station", "")
        holder.stopPointText.text = reformatName

        //If this is the closest station to the phones location, set location marker as visible
        if (stopPointModel.closest && context != null) {
            //-5 is the offset for the location marker to centralise it over the horizontal station line
            val offsetHeight =
                (-5 + 28 * stopPointModel.distanceRatio) * context.resources.displayMetrics.density
            //Get the height of the recycler item in pixels
            val viewHeight = 56 * context.resources.displayMetrics.density

            holder.locationMarker.visibility = View.VISIBLE
            val params = holder.locationMarker.layoutParams as ConstraintLayout.LayoutParams

            params.setMargins(
                (13 * context.resources.displayMetrics.density).toInt(),
                (offsetHeight + viewHeight / 2).toInt(),
                0,
                0
            )
            holder.locationMarker.layoutParams = params
        }

        if (stopPointModel.name == stationName) {
            holder.stopPointText.setTypeface(holder.stopPointText.typeface, Typeface.BOLD)
            holder.stopPointText.textSize = 22f
        }
    }

    override fun getItemCount(): Int {
        return lineStopPointsArray.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val stopPointText: TextView = itemView.findViewById(R.id.line_stop_point_text)
        val locationMarker: View = itemView.findViewById(R.id.line_location_marker_view)
    }
}