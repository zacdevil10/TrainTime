package uk.co.zac_h.traintime.data.model

import com.squareup.moshi.Json

data class NearbyStopsModel(@field:Json(name = "stopPoints") val stopPoints: List<StationModel>)