package uk.co.zac_h.traintime.ui.lines

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.traintime.data.rest.TransportInterface
import uk.co.zac_h.traintime.utils.LatLng
import kotlin.coroutines.CoroutineContext

class TrainLineInteractorImpl : ITrainLine.Interactor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getLineStopPoints(lineName: String, currentLocation: LatLng, callback: ITrainLine.Interactor.Callback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                TransportInterface.create().getLineStopPoints(lineName)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        callback.onSuccess(response.await().body(), currentLocation)
                    } else {
                        callback.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {

                } catch (e: Throwable) {
                    Log.e(
                        this@TrainLineInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelRequest() = coroutineContext.cancel()
}