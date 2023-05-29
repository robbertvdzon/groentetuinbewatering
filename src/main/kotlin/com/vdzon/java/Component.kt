package com.vdzon.java

import java.util.logging.Logger

abstract class Component {
    /**
     * Logger instance
     */
    private val logger = Logger.getLogger(javaClass.name)
    protected fun logInfo(msg: String?) {
        logger.info { msg }
    }

    protected fun logError(msg: String?) {
        logger.severe { msg }
    }

    protected fun logConfig(msg: String?) {
        logger.config { msg }
    }

    protected fun logDebug(msg: String?) {
        logger.fine { msg }
    }

    /**
     * Utility function to sleep for the specified amount of milliseconds.
     * An [InterruptedException] will be caught and ignored while setting the interrupt flag again.
     *
     * @param milliseconds Time in milliseconds to sleep
     */
    fun delay(milliseconds: Long) {
        try {
            Thread.sleep(milliseconds)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}
