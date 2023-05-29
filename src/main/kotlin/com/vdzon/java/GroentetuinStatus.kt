package com.vdzon.java

import kotlinx.serialization.Serializable

@Serializable
data class GroentetuinStatus(
    val klep: String,
    val gisteren: String
)