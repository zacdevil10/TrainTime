package uk.co.zac_h.traintime.data.rest

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.traintime.data.model.ArrivalsModel
import uk.co.zac_h.traintime.data.model.NearbyStopsModel
import uk.co.zac_h.traintime.data.model.StopSequenceModel

interface TransportInterface {

    @GET("/StopPoint/{stationId}/Arrivals")
    suspend fun getStationArrivals(@Path("stationId") stationId: String): Response<List<ArrivalsModel>>

    @GET("/Line/{lineName}/Route/Sequence/all")
    suspend fun getLineStopPoints(@Path("lineName") lineName: String): Response<StopSequenceModel>

    @GET("/StopPoint?stopTypes=NaptanMetroStation&radius=2000&returnLines=true")
    suspend fun getNearbyStops(@Query("lat") lat: Double, @Query("lon") lon: Double): Response<NearbyStopsModel>

    companion object RetrofitSetup {
        fun create(): TransportInterface {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.tfl.gov.uk")
                addConverterFactory(MoshiConverterFactory.create())
            }.build()

            return retrofit.create(TransportInterface::class.java)
        }
    }

}