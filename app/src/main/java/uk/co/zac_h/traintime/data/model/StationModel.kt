package uk.co.zac_h.traintime.data.model

import com.google.gson.annotations.SerializedName

data class StationModel(
    @SerializedName("naptanId") val id: String,
    @SerializedName("commonName") val commonName: String
)