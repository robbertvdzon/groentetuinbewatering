package com.vdzon.java

class Controller(
    val hardware: Hardware
) : encoderListener, switchListener {

    private var value = 0
    private var realValue = 0
    private var manual: Boolean = false

    private var ip: String = ""
    private var time: String =""

    init {
        hardware.registerEncoderListener(this)
        hardware.registerSwitchListener(this)
    }

    fun setIp(ip: String) {
        hardware.updateIP(ip)
    }

    fun klepOpen() {
        hardware.klepOpen()
    }

    fun klepClose() {
        hardware.klepClose()
    }

    override fun encoderUp() {
        if (value <= 0) value = 0
        realValue = Math.round(value / 4.0).toInt()
        println("$realValue")

        if (realValue == 0) {
            klepClose()
        }
        if (realValue == 1) {
            klepOpen()
        }


    }

    override fun encoderDown() {

    }

    override fun switchOn() {

    }

    override fun switchOff() {

    }


}