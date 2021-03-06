package uk.co.zac_h.traintime.utils

object LineColorHelper {

    fun getColour(line: String): String {
        return when (line.toLowerCase()) {
            "bakerloo" -> "#B36305"
            "central" -> "#E32017"
            "circle" -> "#FFD300"
            "district" -> "#00782A"
            "hammersmith & city" -> "#F3A9BB"
            "jubilee" -> "#A0A5A9"
            "metropolitan" -> "#9B0056"
            "northern" -> "#000000"
            "piccadilly" -> "#003688"
            "victoria" -> "#0098D4"
            "waterloo & city" -> "#95CDBA"
            else -> "#008577"
        }
    }

}