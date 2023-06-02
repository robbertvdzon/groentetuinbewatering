package com.vdzon.java

interface Hardware {
    fun klepOpen()
    fun klepClose()

    fun updateTime(time: String)
    fun updateAuto(manual: Boolean)
    fun updateIP(ip: String)
    fun updateKlepState(klepState: KlepState)

    fun encoderUp()
    fun encoderDown()
    fun switchOn()
    fun switchOff()
    fun registerEncoderListener(encoderListener: EncoderListener)
    fun registerSwitchListener(switchListener: SwitchListener)
    fun registerKlepListener(klepListener: KlepListener)
}

interface SwitchListener{
    fun switchOn()
    fun switchOff()
}

interface EncoderListener{
    fun encoderUp()
    fun encoderDown()
}

interface KlepListener{
    fun klepOpening()
    fun klepOpen()
    fun klepClosing()
    fun klepClosed()
}