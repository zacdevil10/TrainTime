package uk.co.zac_h.traintime.ui.lines

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.train_line_fragment.*
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.StopPointModel
import uk.co.zac_h.traintime.utils.LatLng
import uk.co.zac_h.traintime.utils.LineColorHelper
import uk.co.zac_h.traintime.utils.LocationUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class TrainLineFragment : Fragment() {

    companion object {
        fun newInstance(lineName: String, stationName: String) = TrainLineFragment().apply {
            arguments = Bundle().apply {
                putString("lineName", lineName)
                putString("stationName", stationName)
            }
        }
    }

    private lateinit var viewModel: TrainLineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.train_line_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrainLineViewModel::class.java)

        val lineStopPointsArray = ArrayList<StopPointModel>()

        val stopPointsAdapter = StopPointsAdapter(context, arguments?.getString("stationName") ?: "Unknown station", lineStopPointsArray)

        line_toolbar.apply {
            title = arguments?.getString("lineName") ?: "Train Time"
            setBackgroundColor(Color.parseColor(LineColorHelper.getColour(arguments?.getString("lineName") ?: "default")))
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        }

        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor(LineColorHelper.getColour(arguments?.getString("lineName") ?: "default"))
        }

        line_stop_points_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = stopPointsAdapter
        }

        viewModel.getLineStopPoints(arguments?.getString("lineName")?.replace(" & ", "-") ?: "default").subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var stationDistance: Double? = null
                var closestStation: String? = null

                val stopsSequence = LinkedHashMap<String, StopPointModel>()
                val stopsNameSequence = HashMap<String, Int>()
                val stationDistances = TreeMap<Double, String>()

                for (i in 0 until it.sequence[0].points.size) {
                    val stopPoint = it.sequence[0].points[i]
                    val stationLocation = LatLng(stopPoint.lat, stopPoint.lon)

                    //Find closest station
                    val distance = LocationUtils.distance(LatLng(51.521225, -0.162954), stationLocation)

                    if (stationDistance == null) {
                        stationDistance = distance
                        closestStation = stopPoint.name
                    }

                    if (distance < stationDistance) {
                        stationDistance = distance
                        closestStation = stopPoint.name
                    }

                    stationDistances[distance] = stopPoint.name

                    val stopPointModel = StopPointModel(stopPoint.name, stationLocation.lat, stationLocation.lng, false)

                    stopsNameSequence[stopPoint.name] = i
                    stopsSequence[stopPoint.name] = stopPointModel
                }

                val closestStations = ArrayList(stationDistances.values)

                val latLng1 = LocationUtils.closestPoint(stopsSequence[closestStations[0]]?.lat?.let { it1 ->
                    stopsSequence[closestStations[0]]?.lon?.let { it2 ->
                        LatLng(it1,
                            it2
                        )
                    }
                },
                    stopsSequence[closestStations[1]]?.lat?.let { it1 -> stopsSequence[closestStations[1]]?.lon?.let { it2 ->
                        LatLng(it1,
                            it2
                        )
                    } }, LatLng(51.521225, -0.162954))

                val stopPointModel = stopsSequence[closestStation]
                stopPointModel?.closest = true

                var distanceRatio: Double = LocationUtils.distance(latLng1,
                    stopsSequence[closestStations[0]]?.lat?.let { it1 -> stopsSequence[closestStations[0]]?.lon?.let { it2 ->
                        LatLng(it1,
                            it2
                        )
                    } }) / LocationUtils.distance(latLng1,
                    stopsSequence[closestStations[1]]?.lat?.let { it1 -> stopsSequence[closestStations[1]]?.lon?.let { it2 ->
                        LatLng(it1,
                            it2
                        )
                    } })

                if (stopsNameSequence[closestStations[0]]!! > stopsNameSequence[closestStations[1]]!!) distanceRatio = (-distanceRatio)

                stopPointModel?.distanceRatio = distanceRatio

                lineStopPointsArray.addAll(stopsSequence.values)

                stopPointsAdapter.notifyDataSetChanged()
            }){
                Log.e("Error", it.message)
            }
    }

}
