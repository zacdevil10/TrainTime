package uk.co.zac_h.traintime.ui.arrivals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.arrivals_fragment.*
import uk.co.zac_h.traintime.R
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.StationModel
import uk.co.zac_h.traintime.ui.arrivals.adapter.ArrivalsAdapter
import uk.co.zac_h.traintime.utils.LocationUtils

class ArrivalsFragment : Fragment(), IArrivals.View {

    private lateinit var presenter: IArrivals.Presenter

    private lateinit var arrivalsAdapter: ArrivalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.arrivals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ArrivalsPresenterImpl(this, ArrivalsInteractorImpl())

        val currentLocation = LocationUtils.getLocation(context)

        presenter.getNearbyStops(currentLocation)
    }

    override fun setNearbyStopsAdapter(nearby: ArrayList<StationModel>) {
        arrivalsAdapter = ArrivalsAdapter(context, nearby, presenter)

        arrivals_nearby_stations_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = arrivalsAdapter
        }
    }

    override fun updateNearbyStops() {
        arrivalsAdapter.notifyDataSetChanged()
    }

    override fun updateArrivals(arrivals: List<ArrivalsModel>?) {
        println("Arrivals: $arrivals")
        arrivals?.let { arrivalsAdapter.updateArrivals(arrivals) }
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
