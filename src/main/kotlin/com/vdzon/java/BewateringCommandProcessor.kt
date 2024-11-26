package com.vdzon.java

import com.vdzon.java.common.CommandProcessor


class BewateringCommandProcessor(val controller: Controller) : CommandProcessor {
    override fun process(command: String) {
        println("Processing command: $command")

        val count = command.toIntOrNull()
        if (count!=null && count>0){
            println("open $count minuten meer")
            controller.encoderUp(count)
        }
        if (count!=null && count<0){
            println("open $count minuten minder")
            controller.encoderDown(count*-1)
        }
        if (command == "open") {
            controller.openKlep()
        } else if (command == "close") {
            controller.closeKlep()
        }
    }
}