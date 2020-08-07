package uk.co.zac_h.traintime.data.model

import com.squareup.moshi.Json

data class StopSequenceModel(@field:Json(name = "stopPointSequences") val sequence: List<StopPointsModel>)

data class StopPointsModel(@field:Json(name = "stopPoint") val points: List<StopPointModel>)