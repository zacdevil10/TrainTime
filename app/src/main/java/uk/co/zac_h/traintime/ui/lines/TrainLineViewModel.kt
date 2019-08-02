package uk.co.zac_h.traintime.ui.lines

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import uk.co.zac_h.traintime.data.model.StopSequenceModel
import uk.co.zac_h.traintime.data.rest.TransportInterface

class TrainLineViewModel : ViewModel() {

    fun getLineStopPoints(lineName: String): Observable<StopSequenceModel> {
        return TransportInterface.create().getLineStopPoints(lineName)
    }
}
