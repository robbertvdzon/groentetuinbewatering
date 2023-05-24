package com.vdzon.java.schaakspel
import kotlinx.serialization.Serializable

@Serializable
data class SquareField (val field: String, val piece: String)