package com.vdzon.java.ui

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider
import com.pi4j.gpio.extension.pca.PCA9685Pin
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.pi4j.io.i2c.I2CBus
import com.pi4j.io.i2c.I2CFactory
import com.vdzon.java.robotclient.RobotAansturingClient
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class Servo {
    private val log = LoggerFactory.getLogger(RobotAansturingClient::class.java)

    private var provider: PCA9685GpioProvider? = null
    private var busy = false

    @get:Synchronized
    private var request: ServoRequest? = null
    private fun init() {
        try {
            val frequency = BigDecimal("48.828")
            val frequencyCorrectionFactor = BigDecimal("1.0578")
            val bus = I2CFactory.getInstance(I2CBus.BUS_1)
            provider = PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor)
            val myOutputs = provisionPwmOutputs(provider!!)
            provider!!.reset()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Synchronized
    private fun clearRequest() {
        request = null
    }

    @Synchronized
    fun setRequest(startPos: Int, eindPos: Int, time: Long) {
        val servoRequest = ServoRequest()
        servoRequest.startPos = startPos
        servoRequest.eindPos = eindPos
        servoRequest.time = time
        request = servoRequest
    }

    fun runThread() {
        val thread = Thread(Runnable {
            busy = true
            try {
                log.info("Start init in thread")
                init()
                while (true) {
                    val request = request
                    if (request == null) {
                        sleep(5)
                    } else {
                        clearRequest()
                        moveTo(request.startPos, request.eindPos, request.time)
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            busy = false
        })
        thread.start()
    }

    private fun moveTo(oldPos: Int, newPos: Int, time: Long) {
        val startPos = Math.max(oldPos, 900)
        val eindPos = Math.min(newPos, 2100)
        if (startPos == eindPos) return
        val step = if (eindPos > startPos) 1 else -1
        val totalSteps = Math.abs(eindPos - startPos)
        var skipSteps = 1
        var extraDelay: Long = 0
        val totalTime = totalSteps * STUUR_TIME_PER_STEP_IN_MSEC.toLong()
        if (totalTime > time) {
            skipSteps = Math.ceil(totalTime / time.toDouble()).toInt()
        }
        val realSteps = totalSteps / skipSteps
        val totalTimeWithSkip = realSteps * STUUR_TIME_PER_STEP_IN_MSEC.toLong()
        val timeDiff = time - totalTimeWithSkip
        extraDelay = timeDiff / realSteps
        var p = startPos
        while (p != eindPos) {
            if (p % skipSteps == 0) {
                try {
                    provider!!.setPwm(PCA9685Pin.PWM_00, p)
                } catch (ex: Exception) {
                    log.info("ERROR writing " + p + " : " + ex.message)
                }
                if (extraDelay > 0) {
                    sleep(extraDelay)
                }
            }
            p += step
        }
    }

    fun home() {
        provider!!.setPwm(PCA9685Pin.PWM_00, 900)
    }

    companion object {
        const val STUUR_TIME_PER_STEP_IN_MSEC = 2
        private const val SERVO_DURATION_MIN = 900
        private const val SERVO_DURATION_NEUTRAL = 1500
        private const val SERVO_DURATION_MAX = 2100
        private fun sleep(time: Long) {
            try {
                Thread.sleep(time)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        private fun provisionPwmOutputs(gpioProvider: PCA9685GpioProvider): Array<GpioPinPwmOutput> {
            val gpio = GpioFactory.getInstance()
            return arrayOf(
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Pulse 00"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Pulse 01"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Pulse 02"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Pulse 03"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Always OFF"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"),
                    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used"))
        }
    }

    init {
        runThread()
    }
}
