package uk.co.zac_h.traintime.ui.arrivals

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.HttpException
import uk.co.zac_h.traintime.data.rest.TransportInterface
import kotlin.coroutines.CoroutineContext

class ArrivalsInteractorImpl : IArrivals.Interactor {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun getArrivals(id: String, callback: IArrivals.Interactor.Callback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                TransportInterface.create().getStationArrivals(id)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        callback.onArrivalsSuccess(response.await().body())
                    } else {
                        callback.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {

                } catch (e: Throwable) {
                    Log.e(
                        this@ArrivalsInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun getNearbyStops(lat: Double, lng: Double, callback: IArrivals.Interactor.Callback) {
        scope.launch {
            val response = async(SupervisorJob(parentJob)) {
                TransportInterface.create().getNearbyStops(lat, lng)
            }

            withContext(Dispatchers.Main) {
                try {
                    if (response.await().isSuccessful) {
                        callback.onNearbyStopsSuccess(response.await().body())
                    } else {
                        callback.onError("Error: ${response.await().code()}")
                    }
                } catch (e: HttpException) {

                } catch (e: Throwable) {
                    Log.e(
                        this@ArrivalsInteractorImpl.javaClass.name,
                        e.localizedMessage ?: "Job failed to execute"
                    )
                }
            }
        }
    }

    override fun cancelAllRequests() = coroutineContext.cancel()
}