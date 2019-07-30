package uk.co.zac_h.traintime.data.rest

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.NearbyStopsModel
import uk.co.zac_h.traintime.data.model.StationModel
import uk.co.zac_h.traintime.data.model.StopPointModel

interface TransportInterface {

    @GET("/StopPoint/{stationId}/Arrivals")
    fun getStationArrivals(@Path("stationId") stationId: String): Observable<ArrayList<ArrivalsModel>>

    @GET("/Line/{lineName}/Route/Sequence/all")
    fun getLineStopPoints(@Path("lineName") lineName: String): Observable<StopPointModel>

    @GET("/StopPoint?stopTypes=NaptanMetroStation&radius=1000&returnLines=true&")
    fun getNearbyStops(@Query("lat") lat: Double, @Query("lon") lon: Double): Observable<NearbyStopsModel>

    companion object RetrofitSetup {
        fun create(): TransportInterface {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.tfl.gov.uk")
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                addConverterFactory(GsonConverterFactory.create())
            }.build()

            return retrofit.create(TransportInterface::class.java)
        }
    }

}