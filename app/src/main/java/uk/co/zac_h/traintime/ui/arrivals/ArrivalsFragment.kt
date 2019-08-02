package uk.co.zac_h.traintime.ui.arrivals

import android.content.Context
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
import uk.co.zac_h.traintime.FragmentCallback
import uk.co.zac_h.traintime.MainActivity

import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.StationModel
import uk.co.zac_h.traintime.utils.LocationUtils

class ArrivalsFragment : Fragment(), ArrivalsView {

    companion object {
        fun newInstance() = ArrivalsFragment()
    }

    private var fragmentCallback: FragmentCallback? = null

    private lateinit var viewModel: ArrivalsViewModel

    private var arrivalsAdapter: ArrivalsAdapter? = null
    private var nearbyStations = ArrayList<StationModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.arrivals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArrivalsViewModel::class.java)
        viewModel.setup()

        arrivalsAdapter = ArrivalsAdapter(context, nearbyStations, viewModel, this)

        view.findViewById<RecyclerView>(R.id.arrivals_nearby_stations_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = arrivalsAdapter
        }

        val currentLocation = LocationUtils.getLocation(context)

        viewModel.getNearbyStops(currentLocation.lat, currentLocation.lng)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                for (i in 0 until it.stopPoints.size) nearbyStations.add(it.stopPoints[i])
                arrivalsAdapter?.notifyDataSetChanged()
            }){
                Log.e("Error", it.message)
            }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            fragmentCallback = context as MainActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "must implement FragmentCallback")
        }
    }

    override fun showTrainLineFragment(lineName: String, stationName: String) {
        fragmentCallback?.swapFragment(lineName, stationName)
    }
}
