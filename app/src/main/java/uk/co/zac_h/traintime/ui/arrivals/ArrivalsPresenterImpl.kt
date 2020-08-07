package uk.co.zac_h.traintime.ui.arrivals

import android.util.SparseArray
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.NearbyStopsModel
import uk.co.zac_h.traintime.data.model.StationModel
import uk.co.zac_h.traintime.utils.LatLng

class ArrivalsPresenterImpl(
    private val view: IArrivals.View,
    private val interactor: IArrivals.Interactor
) : IArrivals.Presenter, IArrivals.Interactor.Callback {

    private var nearbyStations = ArrayList<StationModel>()

    override fun getArrivals(id: String) {
        interactor.getArrivals(id, this)
    }

    override fun getNearbyStops(latLng: LatLng) {
        view.setNearbyStopsAdapter(nearbyStations)
        interactor.getNearbyStops(latLng.lat, latLng.lng, this)
    }

    override fun onArrivalsSuccess(arrivals: List<ArrivalsModel>?) {
        arrivals?.let {
            val arrivalsList = ArrayList<ArrivalsModel>()
            val orderedArrivals = SparseArray<ArrivalsModel>()

            for (i in it.indices) orderedArrivals.put(it[i].timeToStation, it[i])
            for (i in 0 until 3) arrivalsList.add(orderedArrivals.get(orderedArrivals.keyAt(i)))

            view.updateArrivals(arrivalsList)
        }
    }

    override fun onNearbyStopsSuccess(nearby: NearbyStopsModel?) {
        nearby?.let {
            for (element in it.stopPoints) nearbyStations.add(element)

            view.updateNearbyStops()
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}