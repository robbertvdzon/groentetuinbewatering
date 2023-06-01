package com.vdzon.java

import java.time.LocalDateTime
import kotlin.concurrent.thread

class Controller(
    val hardware: Hardware
) : EncoderListener, SwitchListener, KlepListener {

    private var manual: Boolean = false
    private var klepState = KlepState.OPEN
    private var closeTime: LocalDateTime = LocalDateTime.now().minusDays(1)// in the past

    init {
        hardware.registerEncoderListener(this)
        hardware.registerSwitchListener(this)
        thread(start = true) {
            klepThreadThread()
        }
        hardware.updateAuto(manual)

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

    override fun klepOpening() {
        klepState= KlepState.OPENING
        hardware.updateKlepState("opening")
    }

    override fun klepOpen() {
        klepState= KlepState.OPEN
        hardware.updateKlepState("open")
    }

    override fun klepClosing() {
        klepState= KlepState.CLOSING
        hardware.updateKlepState("closing")
    }

    override fun klepClosed() {
        klepState= KlepState.CLOSED
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
        hardware.updateAuto(manual)
    }

    override fun switchOff() {
        manual = false
        hardware.updateAuto(manual)
    }



    fun klepThreadThread() {
        checkKlep()
        while (true) {
            sleep()
            checkKlep()
        }
    }

    private fun checkKlep() {
        val currentTime = LocalDateTime.now()
        val secondsRemainingUntilClose = currentTime.until(closeTime, java.time.temporal.ChronoUnit.SECONDS)
        val closeTimeInFuture = secondsRemainingUntilClose>0
        val closeTimeInPast = !closeTimeInFuture
        if (klepState== KlepState.OPEN  && closeTimeInPast) {
            hardware.klepClose()
        }
        if (klepState== KlepState.CLOSED && closeTimeInFuture) {
            hardware.klepOpen()
        }
        hardware.updateTime("$secondsRemainingUntilClose seconds")

    }

    private fun sleep() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
        }
    }


}



enum class KlepState {
    OPEN, CLOSED, OPENING, CLOSING
}