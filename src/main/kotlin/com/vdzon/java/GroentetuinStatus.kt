package com.vdzon.java


data class GroentetuinStatus(
    val displayData: DisplayData,
    val lastTimeOpen: String = "??",
    val rainYesterday: String = "??",
    val rainToday: String = "??",
    val rainTomorrow: String = "??"
)