package uk.co.zac_h.traintime.ui.arrivals

import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.NearbyStopsModel
import uk.co.zac_h.traintime.data.model.StationModel
import uk.co.zac_h.traintime.utils.LatLng

interface IArrivals {

    interface View {
        fun setNearbyStopsAdapter(nearby: ArrayList<StationModel>)

        fun updateNearbyStops()
        fun updateArrivals(arrivals: List<ArrivalsModel>?)

        fun showError(error: String)
    }

    interface Presenter {
        fun getArrivals(id: String)

        fun getNearbyStops(latLng: LatLng)
    }

    interface Interactor {

        fun getNearbyStops(lat: Double, lng: Double, callback: Callback)

        fun getArrivals(id: String, callback: Callback)

        fun cancelAllRequests()

        interface Callback {
            fun onArrivalsSuccess(arrivals: List<ArrivalsModel>?)

            fun onNearbyStopsSuccess(nearby: NearbyStopsModel?)

            fun onError(error: String)
        }
    }
}