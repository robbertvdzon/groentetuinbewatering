package com.vdzon.java

interface Hardware {
    fun klepOpen()
    fun klepClose()
    fun updateTime(time: String)
    fun updateAuto(manual: Boolean)
    fun updateIP(ip: String)
    fun encoderUp()
    fun encoderDown()
    fun switchOn()
    fun switchOff()
    fun registerEncoderListener(encoderListener: encoderListener)
    fun registerSwitchListener(switchListener: switchListener)
}

interface switchListener{
    fun switchOn()
    fun switchOff()
}

interface encoderListener{
    fun encoderUp()
    fun encoderDown()
}