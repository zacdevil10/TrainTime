package uk.co.zac_h.traintime.data.model

data class ArrivalsModel(
    var lineName: String,
    var platformName: String,
    var stationName: String,
    var timeToStation: Int
)