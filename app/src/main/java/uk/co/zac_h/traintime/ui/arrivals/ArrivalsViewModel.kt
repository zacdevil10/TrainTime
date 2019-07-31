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

    fun getNearbyStops(): Observable<NearbyStopsModel>? {
        return transportInterface?.getNearbyStops(lat = 51.521225, lon = -0.162954)
    }

    fun getArrivals(id: String): Observable<ArrayList<ArrivalsModel>>? {
        return transportInterface?.getStationArrivals(id)
    }
}
