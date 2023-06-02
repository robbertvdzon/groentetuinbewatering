package com.vdzon.java

import java.net.NetworkInterface

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val remote = System.getenv()["remote"] == "true"
        val hardware = if (remote) HardwareMock() else HardwareImpl()
        val controller = Controller(hardware)
        hardware.start()

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
