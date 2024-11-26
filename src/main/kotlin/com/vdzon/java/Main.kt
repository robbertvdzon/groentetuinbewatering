package com.vdzon.java

import com.vdzon.java.common.FirebaseConfig
import com.vdzon.java.common.FirebaseListener
import java.net.NetworkInterface

object Main {

    private const val SERVICE_ACCOUNT_FILE =
        "/Users/robbertvanderzon/tuinbewatering-firebase-adminsdk-mdooy-b394f2553c.json"
    private const val DATABASE_URL = "https://tuinbewatering.firebaseio.com"
    private const val COLLECTION = "bewatering"
    private const val COMMANDS_DOCUMENT = "commands"

    // initialize the components


    @JvmStatic
    fun main(args: Array<String>) {
        val remote = System.getProperty("remote") == "true"
        val hardware = if (remote) HardwareMock() else HardwareImpl()
        val controller = Controller(hardware)

        val firebaseConfig = FirebaseConfig(SERVICE_ACCOUNT_FILE, DATABASE_URL)
        val commandProcessor = BewateringCommandProcessor(controller)
        val firebaseListener = FirebaseListener(COLLECTION, COMMANDS_DOCUMENT, commandProcessor)

        hardware.start()
        println("NIEUWE VERSIE MET FIREBASE!!")
        // instantiate the Firestore database and start listening for commands
        val dbFirestore = firebaseConfig.initializeFirestore()
        firebaseListener.processCommands(dbFirestore)

        controller.setIp(getCurrentIPv4Address())
        MainWeb(controller).start()

    }


    fun getCurrentIPv4Address(): String {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress.isSiteLocalAddress && inetAddress.hostAddress.indexOf(':') == -1) {
                    return inetAddress.hostAddress
                }
            }
        }
        return "not found"
    }

}
