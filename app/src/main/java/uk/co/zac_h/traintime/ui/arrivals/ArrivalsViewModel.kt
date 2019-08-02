package uk.co.zac_h.traintime.ui.arrivals

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.NearbyStopsModel
import uk.co.zac_h.traintime.data.rest.TransportInterface

class ArrivalsViewModel : ViewModel() {

    private var transportInterface: TransportInterface? = null

    fun setup() {
        transportInterface = TransportInterface.create()
    }

    fun getNearbyStops(lat: Double, lng: Double): Observable<NearbyStopsModel>? {
        return transportInterface?.getNearbyStops(lat = lat, lon = lng)
    }

    fun getArrivals(id: String): Observable<ArrayList<ArrivalsModel>>? {
        return transportInterface?.getStationArrivals(id)
    }
}
