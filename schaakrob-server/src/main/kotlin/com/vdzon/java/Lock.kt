package com.vdzon.java

import java.util.concurrent.locks.ReentrantLock

object Lock {
    private val lock = ReentrantLock()

    fun lock(){
        lock.lock()
    }
    fun unlock(){
        lock.unlock()
    }
}
