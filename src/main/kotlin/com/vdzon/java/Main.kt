package com.vdzon.java

import com.vdzon.java.common.FirebaseConfig
import com.vdzon.java.common.FirebaseListener
import com.vdzon.java.common.FirebaseProducer
import java.net.NetworkInterface
import kotlin.concurrent.thread

object Main {

    private const val SERVICE_ACCOUNT_FILE_OSX =
        "/Users/robbertvanderzon/tuinbewatering-firebase-adminsdk-mdooy-b394f2553c.json"
    private const val SERVICE_ACCOUNT_FILE_LINUX =
        "/home/robbert/tuinbewatering-firebase-adminsdk-mdooy-b394f2553c.json"
    private const val DATABASE_URL = "https://tuinbewatering.firebaseio.com"
    private const val COLLECTION = "bewatering"
    private const val COMMANDS_DOCUMENT = "commands"
    private const val STATUS_DOCUMENT = "status"

    // initialize the components


    @JvmStatic
    fun main(args: Array<String>) {
        val remote = System.getProperty("remote") == "true"
        val hardware = if (remote) HardwareMock() else HardwareImpl()
        // when host is OSX, use the SERVICE_ACCOUNT_FILE_OSX, when linux use SERVICE_ACCOUNT_FILE_LINUX
        println("OS NAME: ${System.getProperty("os.name")}")
        val serviceAccountFile =
            if (System.getProperty("os.name").contains("OS X")) SERVICE_ACCOUNT_FILE_OSX else SERVICE_ACCOUNT_FILE_LINUX
        println("serviceAccountFile: $serviceAccountFile")
        val firebaseConfig = FirebaseConfig(serviceAccountFile, DATABASE_URL)
        val dbFirestore = firebaseConfig.initializeFirestore()
        val firebaseProducer = FirebaseProducer(dbFirestore, COLLECTION, STATUS_DOCUMENT)
        val controller = Controller(hardware, firebaseProducer)

        val commandProcessor = BewateringCommandProcessor(controller)
        val firebaseListener = FirebaseListener(COLLECTION, COMMANDS_DOCUMENT, commandProcessor)

        hardware.start()
        println("NIEUWE VERSIE MET FIREBASE!!")
        // instantiate the Firestore database and start listening for commands
        firebaseListener.processCommands(dbFirestore)
        updateNetworkIp(controller)
    }

    fun updateNetworkIp(controller: Controller) {
        var currentIpAdress = getCurrentIPv4Address()
        println("TEST 1 $currentIpAdress")

        controller.setIp(currentIpAdress)
        thread {
            while (true) {
                val ip = getCurrentIPv4Address()
                println("TEST 2 $ip")
                if (ip != currentIpAdress) {
                    println("new IP: $ip (previous: $currentIpAdress)")
                    controller.setIp(ip)
                    currentIpAdress = ip
                }
                if (ip == "not found"){
                    println("sleep 5")
                    Thread.sleep(5 * 1000)// check every5 seond minute when ip was already found found
                }
                else{
                    println("sleep 60")
                    Thread.sleep(60 * 1000)// check every minute when ip was already found found
                }
            }

        }
    }


    fun getCurrentIPv4Address(): String {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress.isSiteLocalAddress && inetAddress.hostAddress.indexOf(
                        ':'
                    ) == -1
                ) {
                    return inetAddress.hostAddress
                }
            }
        }
        return "not found"
    }

}
