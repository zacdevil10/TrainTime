package uk.co.zac_h.traintime.data.model

data class StopPointModel(val name: String, val lat: Double, val lon: Double, var closest: Boolean) {
    var distanceRatio: Double = 0.0
}