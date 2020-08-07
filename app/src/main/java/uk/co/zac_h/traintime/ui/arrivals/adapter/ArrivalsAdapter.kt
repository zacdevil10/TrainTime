package uk.co.zac_h.traintime.ui.arrivals.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.StationModel
import uk.co.zac_h.traintime.ui.arrivals.IArrivals

class ArrivalsAdapter(
    private val context: Context?,
    private val stations: ArrayList<StationModel>,
    private val presenter: IArrivals.Presenter
) : RecyclerView.Adapter<ArrivalsAdapter.ViewHolder>() {

    private lateinit var trainTimeAdapter: TrainTimeAdapter
    private val arrivals = ArrayList<ArrivalsModel>()

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.arrivals_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (id, commonName) = stations[position]

        holder.stationNameText.text = commonName

        trainTimeAdapter = TrainTimeAdapter(arrivals)

        holder.nextTrainRecycler.apply {
            layoutManager = LinearLayoutManager(this@ArrivalsAdapter.context)
            setHasFixedSize(true)
            adapter = trainTimeAdapter
        }

        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                //Get request for arrivals
                //Repeated every 30 seconds
                handler.postDelayed(this, 30000) //30 seconds in milliseconds

                presenter.getArrivals(id)
            }
        }
        handler.postDelayed(runnable, 0)
    }

    fun updateArrivals(arrivals: List<ArrivalsModel>) {
        this.arrivals.clear()
        this.arrivals.addAll(arrivals)
        trainTimeAdapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = stations.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationNameText: TextView = itemView.findViewById(R.id.station_name_text)
        val nextTrainRecycler: RecyclerView = itemView.findViewById(R.id.next_train_recycler)
    }
}