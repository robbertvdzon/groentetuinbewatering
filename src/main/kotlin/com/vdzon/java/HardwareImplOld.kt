//package com.vdzon.java
//
//import com.pi4j.Pi4J
//import com.pi4j.context.Context
//import com.pi4j.io.gpio.digital.Digital
//import com.pi4j.io.gpio.digital.DigitalInput
//import com.pi4j.io.gpio.digital.DigitalOutput
//import com.pi4j.io.gpio.digital.DigitalState
//import com.pi4j.io.gpio.digital.DigitalStateChangeEvent
//import com.pi4j.io.gpio.digital.DigitalStateChangeListener
//import com.pi4j.io.gpio.digital.PullResistance
//import com.pi4j.library.pigpio.PiGpio
//import com.pi4j.plugin.linuxfs.provider.i2c.LinuxFsI2CProvider
//import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalInputProvider
//import com.pi4j.plugin.pigpio.provider.gpio.digital.PiGpioDigitalOutputProvider
//import com.pi4j.plugin.pigpio.provider.pwm.PiGpioPwmProvider
//import com.pi4j.plugin.pigpio.provider.serial.PiGpioSerialProvider
//import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider
//import com.pi4j.plugin.raspberrypi.platform.RaspberryPiPlatform
//import com.pi4j.util.Console
//import kotlin.concurrent.thread
//
//
//class HardwareImplOld: Hardware{
//
//    companion object {
//        private const val PIN_BUTTON = 6
//        private const val PIN_ENCODER1 = 13
//        private const val PIN_ENCODER2 = 19
//        private const val PIN_AAN_UIT = 17
//        private const val PIN_RICHTING = 12
//    }
//
//    private var pressCount = 0
//
//    var value = 0
//    var realValue = 0
//    var manual: Boolean = false
//    var ip: String = ""
//    var time: String =""
//    var sleepThread: Thread? = null
//    var klepState: String = "?"
//
//    private var encoderListener: EncoderListener? = null
//    private var switchListener: SwitchListener? = null
//
//
//    lateinit var richting: DigitalOutput
//    lateinit var aanUit: DigitalOutput
//    lateinit var pi4j: Context
//
//    init{
//        initHardware()
//    }
//
//    override fun klepOpen() {
//        println("klep open")
//        richting.high()
//        aanUit.high()
//        klepState = "opening"
//        sleepThread?.interrupt()
//        Thread.sleep(3000)
//        klepState = "open"
//        sleepThread?.interrupt()
//        aanUit.low()
//        println("klep opened")
//    }
//
//    override fun klepClose() {
//        println("klep close")
//        richting.low()
//        aanUit.high()
//        klepState = "closing"
//        sleepThread?.interrupt()
//        Thread.sleep(3000)
//        klepState = "closed"
//        sleepThread?.interrupt()
//        aanUit.low()
//        println("klep closed")
//    }
//
//    override fun updateTime(time: String) {
//        this.time = time
//        sleepThread?.interrupt()
//    }
//
//    override fun updateAuto(manual: Boolean) {
//        this.manual = manual
//        sleepThread?.interrupt()
//    }
//
//    override fun updateIP(ip: String) {
//        this.ip = ip
//        sleepThread?.interrupt()
//    }
//
//
//    override fun encoderUp(){
//        encoderListener?.encoderUp()
//    }
//    override fun encoderDown(){
//        encoderListener?.encoderDown()
//    }
//    override fun switchOn(){
//        switchListener?.switchOn()
//    }
//    override fun switchOff(){
//        switchListener?.switchOff()
//    }
//    override fun registerEncoderListener(encoderListener: EncoderListener){
//        this.encoderListener = encoderListener
//    }
//    override fun registerSwitchListener(switchListener: SwitchListener){
//        this.switchListener = switchListener
//    }
//
//
//
//    fun initHardware() {
//        // Initialize Pi4J context
//        val piGpio = PiGpio.newNativeInstance()
//        pi4j = Pi4J.newContextBuilder()
//            .noAutoDetect()
//            .add(object : RaspberryPiPlatform() {
//                override fun getProviders(): Array<String> {
//                    return arrayOf()
//                }
//            })
//            .add(
//                PiGpioDigitalInputProvider.newInstance(piGpio),
//                PiGpioDigitalOutputProvider.newInstance(piGpio),
//                PiGpioPwmProvider.newInstance(piGpio),
//                PiGpioSerialProvider.newInstance(piGpio),
//                PiGpioSpiProvider.newInstance(piGpio),
//                LinuxFsI2CProvider.newInstance()
//            )
//            .build()
//
//        //Create a Component, with amount of ROWS and COLUMNS of the Device
//        val lcd = LcdDisplay(pi4j, 4, 20)
//        lcd.setDisplayBacklight(true)
//
//        // Write text to the lines separate
//        lcd.displayText("Groentetuin!", 1)
//        lcd.displayText("   World!", 2)
//        lcd.displayText("Line 3", 3)
//        lcd.displayText("   Line 4", 4)
//
//
//        val console = Console()
//        console.title("<-- The Pi4J Project -->", "Minimal Example project")
//
//        PrintInfo.printLoadedPlatforms(console, pi4j)
//        PrintInfo.printDefaultPlatform(console, pi4j)
//        PrintInfo.printProviders(console, pi4j)
//
//
//        println("Create sleep thread")
//        sleepThread = thread(start = true) {
//            var lastVal = value
//            var lastManual = manual
//            lcd.displayText("val: $realValue", 4)
//            lcd.displayText("manual: $manual", 3)
//            while (true) {
//
//                try {
//                    // Sleep for 10 milliseconds
//                    Thread.sleep(1000)
//                } catch (e: InterruptedException) {
//                }
//                // Check if the sleep should be interrupted
//                lcd.displayText("ip: $ip", 1)
//                lcd.displayText("klep: $klepState", 2)
//                lcd.displayText("manual: $manual", 3)
//                lcd.displayText("val: $realValue", 4)
//
//            }
//        }
//        println("thread state: ${sleepThread?.state}")
//
//
//        val richtingConfig = DigitalOutput.newConfigBuilder(pi4j)
//            .id("richting")
//            .name("Richting")
//            .address(PIN_AAN_UIT)
//            .shutdown(DigitalState.LOW)
//            .initial(DigitalState.LOW)
//            .provider("pigpio-digital-output")
//
//        richting = pi4j.create(richtingConfig)
//        val aanUitConfig = DigitalOutput.newConfigBuilder(pi4j)
//            .id("aan/uit")
//            .name("Aan/Uit")
//            .address(PIN_RICHTING)
//            .shutdown(DigitalState.LOW)
//            .initial(DigitalState.LOW)
//            .provider("pigpio-digital-output")
//        aanUit = pi4j.create(aanUitConfig)
//
//        val buttonConfig = DigitalInput.newConfigBuilder(pi4j)
//            .id("button")
//            .name("Press button")
//            .address(PIN_BUTTON)
//            .pull(PullResistance.PULL_DOWN)
//            .debounce(3000L)
//            .provider("pigpio-digital-input")
//        val button = pi4j.create(buttonConfig)
//
//        val encoder1Config = DigitalInput.newConfigBuilder(pi4j)
//            .id("encoder1")
//            .name("Encoder1")
//            .address(PIN_ENCODER1)
//            .pull(PullResistance.PULL_UP)
//            .debounce(2000L)
//            .provider("pigpio-digital-input")
//        val encoder1 = pi4j.create(encoder1Config)
//
//        val encoder2Config = DigitalInput.newConfigBuilder(pi4j)
//            .id("encoder2")
//            .name("Encoder2")
//            .address(PIN_ENCODER2)
//            .pull(PullResistance.PULL_UP)
//            .debounce(2000L)
//            .provider("pigpio-digital-input")
//        val encoder2 = pi4j.create(encoder2Config)
//
//        class ButtonListener : DigitalStateChangeListener {
//            override fun onDigitalStateChange(p0: DigitalStateChangeEvent<out Digital<*, *, *>>?) {
//                console.println("CHANGE STATE")
//                manual = p0?.state() === DigitalState.LOW
//                sleepThread?.interrupt()
//                if (p0?.state() === DigitalState.LOW) {
//                    pressCount++
//                    console.println("Button was pressed for the " + pressCount + "th time")
//                }
//            }
//        }
//        button.addListener(ButtonListener())
//
//        var byteCode: Byte = 0b00000000
//        var newByteCode: Byte = 0b00000000
//
//        fun setBit(byte: Byte, position: Int): Byte {
//            val intValue = byte.toInt()
//            val mask = 1 shl position
//            val result = intValue or mask
//            return result.toByte()
//        }
//
//        fun clearBit(byte: Byte, position: Int): Byte {
//            val intValue = byte.toInt()
//            val mask = 1 shl position
//            val invertedMask = mask.inv()
//            val result = intValue and invertedMask
//            return result.toByte()
//        }
//
//        var isOpen = false
//
//        fun updateEncoder() {
//            if (byteCode.toInt() == 1 && newByteCode.toInt() == 3) value++
//            if (byteCode.toInt() == 3 && newByteCode.toInt() == 2) value++
//            if (byteCode.toInt() == 2 && newByteCode.toInt() == 0) value++
//            if (byteCode.toInt() == 0 && newByteCode.toInt() == 1) value++
//
//            if (byteCode.toInt() == 0 && newByteCode.toInt() == 2) value--
//            if (byteCode.toInt() == 2 && newByteCode.toInt() == 3) value--
//            if (byteCode.toInt() == 3 && newByteCode.toInt() == 1) value--
//            if (byteCode.toInt() == 1 && newByteCode.toInt() == 0) value--
//            if (value <= 0) value = 0
//            byteCode = newByteCode
//            realValue = Math.round(value / 4.0).toInt()
//            console.println("$realValue")
//            println(sleepThread)
//            sleepThread?.interrupt()
//
//            if (realValue==0 && isOpen){
//                klepClose()
//                isOpen = false
//            }
//            if (realValue==1 && !isOpen){
//                klepOpen()
//                isOpen = true
//            }
//
//        }
//
//        class Encoder1Listener : DigitalStateChangeListener {
//            override fun onDigitalStateChange(p0: DigitalStateChangeEvent<out Digital<*, *, *>>?) {
//                if (p0?.state() === DigitalState.LOW) {
//                    newByteCode = clearBit(byteCode, 0)
//                    updateEncoder()
//                }
//                if (p0?.state() === DigitalState.HIGH) {
//                    newByteCode = setBit(byteCode, 0)
//                    updateEncoder()
//                }
//            }
//        }
//        encoder1.addListener(Encoder1Listener())
//
//        class Encoder2Listener : DigitalStateChangeListener {
//            override fun onDigitalStateChange(p0: DigitalStateChangeEvent<out Digital<*, *, *>>?) {
//                if (p0?.state() === DigitalState.LOW) {
//                    newByteCode = clearBit(byteCode, 1)
//                    updateEncoder()
//                }
//                if (p0?.state() === DigitalState.HIGH) {
//                    newByteCode = setBit(byteCode, 1)
//                    updateEncoder()
//                }
//            }
//        }
//        encoder2.addListener(Encoder2Listener())
//
//
//        PrintInfo.printRegistry(console, pi4j)
//
//
//    }
//
//    fun close() {
//        pi4j.shutdown()
//    }
//
//
//}
