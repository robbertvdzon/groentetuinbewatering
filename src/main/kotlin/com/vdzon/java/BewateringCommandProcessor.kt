package com.vdzon.java

import com.vdzon.java.common.CommandProcessor


class BewateringCommandProcessor(val controller: Controller) : CommandProcessor {
    override fun process(command: String) {
        println("Processing command: $command")
        if (command == "open") {
            controller.openKlep()
        } else if (command == "close") {
            controller.closeKlep()
        }
    }
}