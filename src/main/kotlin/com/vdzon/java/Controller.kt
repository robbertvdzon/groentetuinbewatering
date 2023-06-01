package com.vdzon.java

import java.time.LocalDateTime

class Controller(
    val hardware: Hardware
) : EncoderListener, SwitchListener, KlepListener {

    private var value = 0
    private var realValue = 0
    private var manual: Boolean = false

    private var ip: String = ""
    private var closeTime: LocalDateTime = LocalDateTime.now().minusDays(1)// in the past

    init {
        hardware.registerEncoderListener(this)
        hardware.registerSwitchListener(this)
    }

    fun setIp(ip: String) {
        hardware.updateIP(ip)
    }

    fun openKlep() {
        hardware.klepOpen()
    }

    fun closeKlep() {
        hardware.klepClose()
    }

    override fun klepOpening(){
     hardware.updateKlepState("opening")
    }

    override fun klepOpen(){
        hardware.updateKlepState("open")
    }

    override fun klepClosing(){
        hardware.updateKlepState("closing")
    }

    override fun klepClosed(){
        hardware.updateKlepState("closed")
    }


    override fun encoderUp() {
        if (closeTime.isBefore(LocalDateTime.now())) {
            closeTime = LocalDateTime.now().plusMinutes(1)
        } else {
            closeTime = closeTime.plusMinutes(1)
        }
        val time = closeTime.toString().substring(11, 16)
        hardware.updateTime(time)
    }

    override fun encoderDown() {
        if (closeTime.isAfter(LocalDateTime.now())) {
            closeTime = LocalDateTime.now().minusMinutes(1)
        } else {
            closeTime = closeTime.minusMinutes(1)
        }
        val time = closeTime.toString().substring(11, 16)
        hardware.updateTime(time)
    }

    override fun switchOn() {
        manual = true
        hardware.updateAuto(false)
    }

    override fun switchOff() {
        manual = false
        hardware.updateAuto(true)
    }


}