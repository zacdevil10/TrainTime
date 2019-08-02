package uk.co.zac_h.traintime.data.model

import com.google.gson.annotations.SerializedName

data class StopSequenceModel(@SerializedName("stopPointSequences") val sequence: ArrayList<StopPointsModel>)

data class StopPointsModel(@SerializedName("stopPoint") val points: ArrayList<StopPointModel>)