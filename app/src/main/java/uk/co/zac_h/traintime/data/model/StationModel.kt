package uk.co.zac_h.traintime.data.model

import com.squareup.moshi.Json

data class StationModel(
    @field:Json(name = "naptanId") val id: String,
    @field:Json(name = "commonName") val commonName: String
)