package uk.co.zac_h.traintime.data.model

import uk.co.zac_h.traintime.utils.LatLng

data class StopPointModel(val name: String, val latLng: LatLng, var closest: Boolean) {
    var distanceRatio: Double = 0.0
}