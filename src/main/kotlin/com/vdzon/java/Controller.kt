package com.vdzon.java

import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.concurrent.thread
import java.time.Duration

class Controller(
    val hardware: Hardware
) : EncoderListener, SwitchListener, KlepListener {

    private var manual: Boolean = false
    private var klepState = KlepState.OPEN
    private var closeTime: LocalDateTime = LocalDateTime.now().minusDays(1)// in the past

    init {
        hardware.registerEncoderListener(this)
        hardware.registerSwitchListener(this)
        hardware.registerKlepListener(this)
        thread(start = true) {
            klepThreadThread()
        }
        hardware.updateAuto(manual)

    }

    fun getDisplayData(): DisplayData {
        return hardware.getDisplayData()
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
        hardware.updateKlepState(KlepState.OPENING)
    }

    override fun klepOpen() {
        klepState= KlepState.OPEN
        hardware.updateKlepState(KlepState.OPEN)
    }

    override fun klepClosing() {
        klepState= KlepState.CLOSING
        hardware.updateKlepState(KlepState.CLOSING)
    }

    override fun klepClosed() {
        klepState= KlepState.CLOSED
        hardware.updateKlepState(KlepState.CLOSED)
    }


    override fun encoderUp() {
        if (closeTime.isBefore(LocalDateTime.now())) {
            closeTime = LocalDateTime.now().plusMinutes(1)
        } else {
            closeTime = closeTime.plusMinutes(1)
        }
        val time = closeTime.toString().substring(11, 16)
        displayTime()
    }

    override fun encoderDown() {
        closeTime = closeTime.minusMinutes(1)
        val time = closeTime.toString().substring(11, 16)
        displayTime()
    }

    override fun switchOn() {
        println("controller: switch on")
        manual = true
        hardware.updateAuto(manual)
    }

    override fun switchOff() {
        println("controller: switch off")
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

        displayTime()
    }

    private fun displayTime() {
        val currentTime = LocalDateTime.now()
        val secondsRemainingUntilClose = currentTime.until(closeTime, java.time.temporal.ChronoUnit.SECONDS)
        val closeTimeInFuture = secondsRemainingUntilClose>0
        if (closeTimeInFuture){
            val duration: Duration = Duration.between(currentTime, closeTime)
            val hours = duration.toHours()
            val minutes = duration.toMinutesPart()
            val seconds = duration.toSecondsPart()

            val formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds)

            hardware.updateTime("$formattedDuration")
        }else{
            hardware.updateTime("")
        }
    }


        private fun sleep() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
        }
    }


}



enum class KlepState(val text: String) {
    OPEN ("Klep open"), CLOSED ("Klep dicht"), OPENING ("Openen"), CLOSING ("Sluiten")
}