package uk.co.zac_h.traintime.ui.arrivals

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable
import uk.co.zac_h.traintime.data.model.NearbyStopsModel
import uk.co.zac_h.traintime.data.model.StationModel
import uk.co.zac_h.traintime.data.rest.TransportInterface

class ArrivalsViewModel : ViewModel() {

    fun getNearbyStops(): Observable<NearbyStopsModel> {
        return TransportInterface.create().getNearbyStops(lat = 51.521225, lon = -0.162954)
    }
}
