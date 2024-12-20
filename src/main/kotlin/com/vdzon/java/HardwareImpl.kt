package com.vdzon.java

import com.pi4j.Pi4J
import com.pi4j.context.Context
import com.pi4j.io.gpio.digital.Digital
import com.pi4j.io.gpio.digital.DigitalInput
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder
import com.pi4j.io.gpio.digital.DigitalOutput
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder
import com.pi4j.io.gpio.digital.DigitalState
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent
import com.pi4j.io.gpio.digital.DigitalStateChangeListener
import com.pi4j.io.gpio.digital.PullResistance
import com.pi4j.library.pigpio.PiGpio
import com.pi4j.plugin.linuxfs.provider.i2c.LinuxFsI2CProvider
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider
import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider
import com.pi4j.plugin.pigpio.provider.pwm.PiGpioPwmProvider
import com.pi4j.plugin.pigpio.provider.serial.PiGpioSerialProvider
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider
import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform
import com.pi4j.util.Console
import kotlin.concurrent.thread


class HardwareImpl : Hardware {

    companion object {
        private const val PIN_BUTTON = 6
        private const val PIN_ENCODER1 = 13
        private const val PIN_ENCODER2 = 19
        private const val PIN_AAN_UIT = 17
        private const val PIN_RICHTING = 12
    }

    var displayThread: Thread? = null

    private var encoderListener: EncoderListener? = null
//    private var switchListener: SwitchListener? = null
//    private var klepListener: KlepListener? = null

    lateinit var pi4j: Context
    lateinit var displayController: DisplayController
//    lateinit var richting: DigitalOutput
//    lateinit var aanUit: DigitalOutput
//    lateinit var switchButton: DigitalInput
//    lateinit var encoder1: DigitalInput
//    lateinit var encoder2: DigitalInput

    init {
        initHardware()
    }

    override fun klepOpen() {
//        Thread {
//            println("klep open")
//            klepListener?.klepOpening()
//            richting.high()
//            aanUit.high()
//            Thread.sleep(3000)
//            aanUit.low()
//            klepListener?.klepOpen()
//            println("klep opened")
//        }.start()
    }

    override fun klepClose() {
//        Thread {
//            println("klep close")
//            klepListener?.klepClosing()
//            richting.low()
//            aanUit.high()
//            Thread.sleep(3000)
//            aanUit.low()
//            klepListener?.klepClosed()
//            println("klep closed")
//        }.start()

    }

    override fun updateTime(time: String) {
        displayController.displayData.time = time
        displayThread?.interrupt()
    }

    override fun updatePlannedTime(plannedTime: String){
        displayController.displayData.plannedTime = plannedTime
        displayThread?.interrupt()
    }

    override fun updateAuto(manual: Boolean) {
        println("impl: manual = $manual")
        displayController.displayData.manual = manual
        displayThread?.interrupt()
    }

    override fun updateIP(ip: String) {
        println("IP adress: $ip")
        displayController.displayData.ip = ip
        displayThread?.interrupt()
    }

    override fun updateKlepState(klepState: KlepState) {
        displayController.displayData.klepState = klepState
        displayThread?.interrupt()
    }


    override fun encoderUp() {
        encoderListener?.encoderUp()
    }

    override fun encoderDown() {
        encoderListener?.encoderDown()

    }

    override fun switchOn() {
//        switchListener?.switchOn()
    }

    override fun switchOff() {
//        switchListener?.switchOff()
    }


    override fun registerEncoderListener(encoderListener: EncoderListener) {
        this.encoderListener = encoderListener
    }

    override fun registerSwitchListener(switchListener: SwitchListener) {
//        this.switchListener = switchListener
    }

    override fun registerKlepListener(klepListener: KlepListener) {
//        this.klepListener = klepListener
    }

    override fun getDisplayData(): DisplayData = displayController.displayData

    override fun start(){
//        val switchState = switchButton.isHigh
//        println(switchState)
//        if (switchButton.isHigh) switchOff() else switchOn()
    }


    private fun switchChanged(state: DigitalState?) {
        if (state === DigitalState.LOW) switchOn() else switchOff()
    }



    fun initHardware() {
        val piGpio = PiGpio.newNativeInstance()
        pi4j = buildPi4j(piGpio)
        val lcd = LcdDisplay(pi4j, 4, 20)
        displayController = DisplayController(lcd)
        displayThread = displayController.startThread()
        printInfo()

//        val richtingConfig = getRichtingConfig()
//        val aanUitConfig = getAanUitConfig()
//        val switchButtonConfig = getButtonConfig()
//        val encoder1Config = getEncoder1Config()
//        val encoder2Config = encoder2Config()
//
//        richting = pi4j.create(richtingConfig)
//        aanUit = pi4j.create(aanUitConfig)
//        switchButton = pi4j.create(switchButtonConfig)
//        encoder1 = pi4j.create(encoder1Config)
//        encoder2 = pi4j.create(encoder2Config)
//
        class SwitchButtonListener : DigitalStateChangeListener {
            override fun onDigitalStateChange(p0: DigitalStateChangeEvent<out Digital<*, *, *>>?) {
                switchChanged(p0?.state())
            }
        }
//        switchButton.addListener(SwitchButtonListener())

        var byteCode: Byte = 0b00000000
        var newByteCode: Byte = 0b00000000

        fun setBit(byte: Byte, position: Int): Byte {
            val intValue = byte.toInt()
            val mask = 1 shl position
            val result = intValue or mask
            return result.toByte()
        }

        fun clearBit(byte: Byte, position: Int): Byte {
            val intValue = byte.toInt()
            val mask = 1 shl position
            val invertedMask = mask.inv()
            val result = intValue and invertedMask
            return result.toByte()
        }

        var encoderValue = 0
        var lastWholeValue = 0

        fun encoderMicroUp() {
            encoderValue++
            println("encoderValue: $encoderValue")
            val wholeValue = encoderValue / 4
            if (wholeValue != lastWholeValue) {
                lastWholeValue = wholeValue
                println("call encoderUp")
                encoderUp()

            }
        }

        fun encoderMicroDown() {
            encoderValue--
            println("encoderValue: $encoderValue")
            val wholeValue = encoderValue / 4
            if (wholeValue != lastWholeValue) {
                lastWholeValue = wholeValue
                println("call encoderDown")
                encoderDown()
            }
        }

        fun updateEncoder() {
            if (byteCode.toInt() == 1 && newByteCode.toInt() == 3) encoderMicroUp()
            if (byteCode.toInt() == 3 && newByteCode.toInt() == 2) encoderMicroUp()
            if (byteCode.toInt() == 2 && newByteCode.toInt() == 0) encoderMicroUp()
            if (byteCode.toInt() == 0 && newByteCode.toInt() == 1) encoderMicroUp()

            if (byteCode.toInt() == 0 && newByteCode.toInt() == 2) encoderMicroDown()
            if (byteCode.toInt() == 2 && newByteCode.toInt() == 3) encoderMicroDown()
            if (byteCode.toInt() == 3 && newByteCode.toInt() == 1) encoderMicroDown()
            if (byteCode.toInt() == 1 && newByteCode.toInt() == 0) encoderMicroDown()
            byteCode = newByteCode
        }

        class Encoder1Listener : DigitalStateChangeListener {
            override fun onDigitalStateChange(p0: DigitalStateChangeEvent<out Digital<*, *, *>>?) {
                if (p0?.state() === DigitalState.LOW) {
                    newByteCode = clearBit(byteCode, 0)
                    updateEncoder()
                }
                if (p0?.state() === DigitalState.HIGH) {
                    newByteCode = setBit(byteCode, 0)
                    updateEncoder()
                }
            }
        }
//        encoder1.addListener(Encoder1Listener())

        class Encoder2Listener : DigitalStateChangeListener {
            override fun onDigitalStateChange(p0: DigitalStateChangeEvent<out Digital<*, *, *>>?) {
                if (p0?.state() === DigitalState.LOW) {
                    newByteCode = clearBit(byteCode, 1)
                    updateEncoder()
                }
                if (p0?.state() === DigitalState.HIGH) {
                    newByteCode = setBit(byteCode, 1)
                    updateEncoder()
                }
            }
        }
//        encoder2.addListener(Encoder2Listener())
    }

    private fun encoder2Config(): DigitalInputConfigBuilder? = DigitalInput.newConfigBuilder(pi4j)
        .id("encoder2")
        .name("Encoder2")
        .address(PIN_ENCODER2)
        .pull(PullResistance.PULL_UP)
        .debounce(2000L)
        .provider("pigpio-digital-input")

    private fun getEncoder1Config(): DigitalInputConfigBuilder? = DigitalInput.newConfigBuilder(pi4j)
        .id("encoder1")
        .name("Encoder1")
        .address(PIN_ENCODER1)
        .pull(PullResistance.PULL_UP)
        .debounce(2000L)
        .provider("pigpio-digital-input")

    private fun getButtonConfig(): DigitalInputConfigBuilder? = DigitalInput.newConfigBuilder(pi4j)
        .id("button")
        .name("Press button")
        .address(PIN_BUTTON)
        .pull(PullResistance.PULL_DOWN)
        .debounce(3000L)
        .provider("pigpio-digital-input")

    private fun getAanUitConfig(): DigitalOutputConfigBuilder? = DigitalOutput.newConfigBuilder(pi4j)
        .id("aan/uit")
        .name("Aan/Uit")
        .address(PIN_RICHTING)
        .shutdown(DigitalState.LOW)
        .initial(DigitalState.LOW)
        .provider("pigpio-digital-output")

    private fun getRichtingConfig(): DigitalOutputConfigBuilder? = DigitalOutput.newConfigBuilder(pi4j)
        .id("richting")
        .name("Richting")
        .address(PIN_AAN_UIT)
        .shutdown(DigitalState.LOW)
        .initial(DigitalState.LOW)
        .provider("pigpio-digital-output")


    private fun buildPi4j(piGpio: PiGpio?): Context = Pi4J.newContextBuilder()
        .noAutoDetect()
        .add(object : RaspberryPiPlatform() {
            override fun getProviders(): Array<String> {
                return arrayOf()
            }
        })
        .add(
            PiGpioDigitalInputProvider.newInstance(piGpio),
            PiGpioDigitalOutputProvider.newInstance(piGpio),
            PiGpioPwmProvider.newInstance(piGpio),
            PiGpioSerialProvider.newInstance(piGpio),
            PiGpioSpiProvider.newInstance(piGpio),
            LinuxFsI2CProvider.newInstance()
        )
        .build()

    private fun printInfo() {
        val console = Console()
        console.title("<-- The Pi4J Project -->", "Minimal Example project")
        PrintInfo.printLoadedPlatforms(console, pi4j)
        PrintInfo.printDefaultPlatform(console, pi4j)
        PrintInfo.printProviders(console, pi4j)
        PrintInfo.printRegistry(console, pi4j)
    }


    fun close() {
        pi4j.shutdown()
    }


}

data class DisplayData(
    var manual: Boolean = false,
    var ip: String = "",
    var klepState: KlepState = KlepState.OPEN,
    var time: String = "",
    var plannedTime: String = ""

)

class DisplayController(val lcd: LcdDisplay) {

    val displayData = DisplayData()

    fun startThread() = thread(start = true) {
        displayThread()
    }


    fun displayThread() {
        lcd.setDisplayBacklight(true)
        lcd.clearDisplay()
        updateDisplay()
        while (true) {
            sleep()
            updateDisplay()
        }
    }

    private fun updateDisplay() {

        val state = if (displayData.manual)
            "Handmatig  "
        else
            "Automatisch"



        lcd.displayText("${displayData.ip}", 1)
        lcd.displayText("${displayData.klepState.text}", 2)
        lcd.displayText("$state", 3)
        if (displayData.time.isNotEmpty()) {
            lcd.displayText("Timer: ${displayData.time}", 4)
        }
        else if (displayData.plannedTime.isNotEmpty()) {
            lcd.displayText("Planned: ${displayData.plannedTime}", 4)
        }
        else{
            lcd.displayText("Geen planning", 4)
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
        }
    }

}