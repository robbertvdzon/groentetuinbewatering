package com.vdzon.java.robotimpl

import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.PinPullResistance
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent
import com.pi4j.io.gpio.event.GpioPinListenerDigital
import com.vdzon.java.robitapi.RobotAansturing
import de.pi3g.pi.oled.Font
import de.pi3g.pi.oled.OLEDDisplay
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.util.*

class Display(private val robotAansturing: RobotAansturing) {
    private val display = OLEDDisplay()
    private var pageNr = 0

    fun button1(event: GpioPinDigitalStateChangeEvent){
    }

    fun button2(event: GpioPinDigitalStateChangeEvent){
        if (event.state==PinState.HIGH) {
            nextPage()
            showPage()
        }
    }
    fun button3(event: GpioPinDigitalStateChangeEvent){
        if (event.state==PinState.HIGH) {
            prevPage()
            showPage()
        }
    }
    fun button4(event: GpioPinDigitalStateChangeEvent){
        val maxPages = 11
        when(pageNr % maxPages){
            1-> robotAansturing.runDemoOnce()
            2-> robotAansturing.stopDemo()
            3-> robotAansturing.resetBoard()
            4-> robotAansturing.homeHor()
            5-> robotAansturing.homeVert()
            6-> robotAansturing.clamp1()
            7-> robotAansturing.release1()
            8-> robotAansturing.clamp2()
            9-> robotAansturing.release2()
            10-> robotAansturing.rebuild()
        }

    }

    fun nextPage(){
        pageNr++
    }
    fun prevPage(){
        pageNr--
    }


    fun showPage(){
        val maxPages = 10
        when(pageNr % maxPages){
            0-> displayText(localHostLANAddress().hostAddress)
            1-> displayText("start autoplay")
            2-> displayText("stop autoplay")
            3-> displayText("reset board")
            4-> displayText("home hor")
            5-> displayText("home ver")
            6-> displayText("pak 1")
            7-> displayText("release 1")
            8-> displayText("pak 2")
            9-> displayText("release 2")
            10-> displayText("rebuild")
            else-> displayText("unknown!")
        }
    }


    fun localHostLANAddress(): InetAddress {
        try {
            var candidateAddress: InetAddress? = null
            // Iterate all NICs (network interface cards)...
            val ifaces: Enumeration<*> = NetworkInterface.getNetworkInterfaces()
            while (ifaces.hasMoreElements()) {
                val iface = ifaces.nextElement() as NetworkInterface
                // Iterate all IP addresses assigned to each card...
                val inetAddrs: Enumeration<*> = iface.inetAddresses
                while (inetAddrs.hasMoreElements()) {
                    val inetAddr = inetAddrs.nextElement() as InetAddress
                    if (!inetAddr.isLoopbackAddress) {
                        if (inetAddr.isSiteLocalAddress) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            val jdkSuppliedAddress = InetAddress.getLocalHost()
                ?: throw UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.")
            return jdkSuppliedAddress
        } catch (e: Exception) {
            val unknownHostException = UnknownHostException("Failed to determine LAN address: $e")
            unknownHostException.initCause(e)
            throw unknownHostException
        }
    }


    fun displayText(text: String){
        try{
            display.clear()
            display.drawStringCentered(text, Font.FONT_5X8, 25, true)
            display.update()
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    fun startDisplay(){
        try{
            display.drawStringCentered("Schaakrob!!", Font.FONT_5X8, 25, true)
            display.update()
        }
        catch (e:Exception){
            e.printStackTrace()
        }

        try{
            println("<--Pi4J--> GPIO Listen Example ... started.")
            val gpio = GpioFactory.getInstance()
            val myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN)
            myButton.setShutdownOptions(true)
            myButton.addListener(GpioPinListenerDigital(this::button1))
            val myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN)
            myButton2.setShutdownOptions(true)
            myButton2.addListener(GpioPinListenerDigital(this::button2))
            val myButton3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN)
            myButton3.setShutdownOptions(true)
            myButton3.addListener(GpioPinListenerDigital(this::button3))
            val myButton4 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN)
            myButton4.setShutdownOptions(true)
            myButton4.addListener(GpioPinListenerDigital(this::button4))

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }
}