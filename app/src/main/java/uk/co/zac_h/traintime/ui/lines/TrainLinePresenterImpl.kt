package uk.co.zac_h.traintime.ui.lines

import uk.co.zac_h.traintime.data.model.StopPointModel
import uk.co.zac_h.traintime.data.model.StopSequenceModel
import uk.co.zac_h.traintime.utils.LatLng
import uk.co.zac_h.traintime.utils.LocationUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class TrainLinePresenterImpl(
    private val view: ITrainLine.View,
    private val interactor: ITrainLine.Interactor
) : ITrainLine.Presenter, ITrainLine.Interactor.Callback {

    private val lineStopPointsArray = ArrayList<StopPointModel>()

    override fun getLineStopPoints(lineName: String, currentLocation: LatLng) {
        interactor.getLineStopPoints(lineName, currentLocation, this)

        view.setLinePointsAdapter(lineStopPointsArray)
    }

    override fun onSuccess(stopSequence: StopSequenceModel?, currentLocation: LatLng) {
        stopSequence?.let {
            var stationDistance: Double? = null
            var closestStation: String? = null

            val stopsSequence = LinkedHashMap<String, StopPointModel>()
            val stopsNameSequence = HashMap<String, Int>()
            val stationDistances = TreeMap<Double, String>()

            for (i in 0 until it.sequence[0].points.size) {
                val stopPoint = it.sequence[0].points[i]
                val stationLocation = LatLng(stopPoint.lat, stopPoint.lon)

                //Find closest station
                val distance = LocationUtils.distance(
                    LatLng(currentLocation.lat, currentLocation.lng),
                    stationLocation
                )

                if (stationDistance == null) {
                    stationDistance = distance
                    closestStation = stopPoint.name
                }

                if (distance < stationDistance) {
                    stationDistance = distance
                    closestStation = stopPoint.name
                }

                stationDistances[distance] = stopPoint.name

                val stopPointModel = StopPointModel(
                    stopPoint.name,
                    stationLocation.lat,
                    stationLocation.lng,
                    false
                )

                stopsNameSequence[stopPoint.name] = i
                stopsSequence[stopPoint.name] = stopPointModel
            }

            val closestStations = ArrayList(stationDistances.values)

            val latLng1 =
                LocationUtils.closestPoint(
                    stopsSequence[closestStations[0]]?.lat?.let { it1 ->
                        stopsSequence[closestStations[0]]?.lon?.let { it2 ->
                            LatLng(
                                it1,
                                it2
                            )
                        }
                    },
                    stopsSequence[closestStations[1]]?.lat?.let { it1 ->
                        stopsSequence[closestStations[1]]?.lon?.let { it2 ->
                            LatLng(
                                it1,
                                it2
                            )
                        }
                    }, LatLng(currentLocation.lat, currentLocation.lng)
                )

            val stopPointModel = stopsSequence[closestStation]
            stopPointModel?.closest = true

            var distanceRatio: Double = LocationUtils.distance(latLng1,
                stopsSequence[closestStations[0]]?.lat?.let { it1 ->
                    stopsSequence[closestStations[0]]?.lon?.let { it2 ->
                        LatLng(
                            it1,
                            it2
                        )
                    }
                }) / LocationUtils.distance(latLng1,
                stopsSequence[closestStations[1]]?.lat?.let { it1 ->
                    stopsSequence[closestStations[1]]?.lon?.let { it2 ->
                        LatLng(
                            it1,
                            it2
                        )
                    }
                })

            if (stopsNameSequence[closestStations[0]]!! > stopsNameSequence[closestStations[1]]!!) distanceRatio =
                (-distanceRatio)

            stopPointModel?.distanceRatio = distanceRatio

            lineStopPointsArray.addAll(stopsSequence.values)
        }

        view.updateLinePointsAdapter()
    }

    override fun onError(error: String) {

    }
}