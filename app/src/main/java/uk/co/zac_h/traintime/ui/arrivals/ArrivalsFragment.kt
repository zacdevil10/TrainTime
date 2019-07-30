package uk.co.zac_h.traintime.ui.arrivals

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.StationModel

class ArrivalsFragment : Fragment() {

    companion object {
        fun newInstance() = ArrivalsFragment()
    }

    private lateinit var viewModel: ArrivalsViewModel

    private var arrivalsAdapter: ArrivalsAdapter? = null
    private var nearbyStations = ArrayList<StationModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.arrivals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrivalsAdapter = ArrivalsAdapter(nearbyStations)

        view.findViewById<RecyclerView>(R.id.arrivals_nearby_stations_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = arrivalsAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArrivalsViewModel::class.java)

        viewModel.getNearbyStops()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for (i in 0 until it.stopPoints.size) nearbyStations.add(it.stopPoints[i])
                arrivalsAdapter?.notifyDataSetChanged()
            }){
                Log.e("Error", it.message)
            }
    }

}
