package com.vdzon.java

interface Hardware {
    fun start()

    fun klepOpen()
    fun klepClose()

    fun updateTime(time: String)
    fun updatePlannedTime(plannedTime: String)
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
    fun getDisplayData(): DisplayData
}

interface SwitchListener{
    fun switchOn()
    fun switchOff()
}

interface EncoderListener{
    fun encoderUp(amount: Int = 1)
    fun encoderDown(amount: Int = 1)
    fun dicht()
}

interface KlepListener{
    fun klepOpening()
    fun klepOpen()
    fun klepClosing()
    fun klepClosed()
}