package uk.co.zac_h.traintime.ui.arrivals

import android.content.Context
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.StationModel

class ArrivalsAdapter(
    private val context: Context?,
    private val stations: ArrayList<StationModel>,
    private val viewModel: ArrivalsViewModel,
    private val view: ArrivalsView
): RecyclerView.Adapter<ArrivalsAdapter.ViewHolder>() {

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.arrivals_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderedArrivals = SparseArray<ArrivalsModel>()
        val arrivals = ArrayList<ArrivalsModel>()

        val (id, commonName) = stations[position]

        holder.stationNameText.text = commonName

        val timeAdapter = TrainTimeAdapter(arrivals, view)

        holder.nextTrainRecycler.apply {
            layoutManager = LinearLayoutManager(this@ArrivalsAdapter.context)
            setHasFixedSize(true)
            adapter = timeAdapter
        }

        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                //Get request for arrivals
                //Repeated every 30 seconds
                handler?.postDelayed(this, 30000) //30 seconds in milliseconds

                arrivals.clear()
                orderedArrivals.clear()

                viewModel.getArrivals(id)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        for (i in 0 until it.size) orderedArrivals.put(it[i].timeToStation, it[i])
                        for (i in 0 until 3) arrivals.add(orderedArrivals.get(orderedArrivals.keyAt(i)))

                        timeAdapter.notifyDataSetChanged()
                    }) {
                        Log.e("Error", it.message)
                    }
            }
        }
        handler?.postDelayed(runnable, 0)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val stationNameText: TextView = itemView.findViewById(R.id.station_name_text)
        val nextTrainRecycler: RecyclerView = itemView.findViewById(R.id.next_train_recycler)
    }
}