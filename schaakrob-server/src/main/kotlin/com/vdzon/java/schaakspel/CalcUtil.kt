package com.vdzon.java.schaakspel

import kotlin.math.absoluteValue

object CalcUtil {

    fun calcDistance(pos1: String, pos2: String): Double {
        val pos1X = "ABCDEFGH".indexOf(pos1.get(0)).toDouble()
        val pos2X = "ABCDEFGH".indexOf(pos2.get(0)).toDouble()
        val pos1Y = pos1.substring(1).toDouble()
        val pos2Y = pos2.substring(1).toDouble()

        val diffX = (pos1X-pos2X).absoluteValue
        val diffY = (pos1Y-pos2Y).absoluteValue
        return Math.sqrt(diffX*diffX+diffY*diffY)
    }


}