package uk.co.zac_h.traintime.ui.lines

import uk.co.zac_h.traintime.data.model.StopPointModel
import uk.co.zac_h.traintime.data.model.StopSequenceModel
import uk.co.zac_h.traintime.utils.LatLng

interface ITrainLine {

    interface View {
        fun setLinePointsAdapter(lineStopPoints: ArrayList<StopPointModel>)
        fun updateLinePointsAdapter()
    }

    interface Presenter {
        fun getLineStopPoints(lineName: String, currentLocation: LatLng)
    }

    interface Interactor {

        fun getLineStopPoints(lineName: String, currentLocation: LatLng, callback: Callback)

        fun cancelRequest()

        interface Callback {
            fun onSuccess(stopSequence: StopSequenceModel?, currentLocation: LatLng)
            fun onError(error: String)
        }
    }
}