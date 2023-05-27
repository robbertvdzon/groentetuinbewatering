package com.vdzon.java

import com.pi4j.Pi4J
import com.pi4j.io.gpio.digital.Digital
import com.pi4j.io.gpio.digital.DigitalInput
import com.pi4j.io.gpio.digital.DigitalOutput
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


object Main {

    private const val PIN_BUTTON = 6 // PIN 18 = BCM 24
    private const val PIN_AAN_UIT = 12
    private const val PIN_RICHTING = 17
    private var pressCount = 0

    @JvmStatic
    fun main(args: Array<String>) {
        val remote = System.getenv()["remote"] ?: "false"
        println("remote=$remote")


        // Initialize Pi4J context
        val piGpio = PiGpio.newNativeInstance()
        val pi4j2 = Pi4J.newContextBuilder()
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

        //Create a Component, with amount of ROWS and COLUMNS of the Device
        val lcd = LcdDisplay(pi4j2, 4, 20)
        lcd.setDisplayBacklight(true)

        // Write text to the lines separate
        lcd.displayText("Groentetuin!", 1)
        lcd.displayText("   World!", 2)
        lcd.displayText("Line 3", 3)
        lcd.displayText("   Line 4", 4)


        val console = Console()
        console.title("<-- The Pi4J Project -->", "Minimal Example project")

        var pi4j = Pi4J.newAutoContext()

        PrintInfo.printLoadedPlatforms(console, pi4j)
        PrintInfo.printDefaultPlatform(console, pi4j)
        PrintInfo.printProviders(console, pi4j)

        val richtingConfig = DigitalOutput.newConfigBuilder(pi4j)
            .id("richting")
            .name("Richting")
            .address(PIN_AAN_UIT)
            .shutdown(DigitalState.LOW)
            .initial(DigitalState.LOW)
            .provider("pigpio-digital-output")
        val richting = pi4j.create(richtingConfig)

        val aanUitConfig = DigitalOutput.newConfigBuilder(pi4j)
            .id("aan/uit")
            .name("Aan/Uit")
            .address(PIN_RICHTING)
            .shutdown(DigitalState.LOW)
            .initial(DigitalState.LOW)
            .provider("pigpio-digital-output")
        val aanUit = pi4j.create(aanUitConfig)

        val buttonConfig = DigitalInput.newConfigBuilder(pi4j)
            .id("button")
            .name("Press button")
            .address(PIN_BUTTON)
            .pull(PullResistance.PULL_DOWN)
            .debounce(3000L)
            .provider("pigpio-digital-input")
        val button = pi4j.create(buttonConfig)

        class MyListener: DigitalStateChangeListener {
            override fun onDigitalStateChange(p0: DigitalStateChangeEvent<out Digital<*, *, *>>?) {
                console.println("CHANGE STATE")
                if (p0?.state() === DigitalState.LOW) {
                    pressCount++
                    console.println("Button was pressed for the " + pressCount + "th time")
                }
            }
        }

        val l: DigitalStateChangeListener =  MyListener()


        button.addListener(l)

        PrintInfo.printRegistry(console, pi4j)

        richting.low()
        aanUit.low()

        Thread.sleep(2000)
        richting.high()
        aanUit.low()

        Thread.sleep(2000)
        richting.high()
        aanUit.low()

        Thread.sleep(2000)
        richting.high()
        aanUit.high()


        while (pressCount < 5) {
//            if (led.equals(DigitalState.HIGH)) {
//                console.println("LED low")
//                led.low()
//            } else {
//                console.println("LED high")
//                led.high()
//            }
            java.lang.Thread.sleep((500 / (pressCount + 1)).toLong())
        }

        pi4j.shutdown()

    }
}
