package com.vdzon.java


class HardwareMock : Hardware {
    private var encoderListener: encoderListener? = null
    private var switchListener: switchListener? = null


    override fun klepOpen() {
        println("klep open")
    }

    override fun klepClose() {
        println("klep close")
    }

    override fun updateTime(time: String) {
        println("time: $time")
    }

    override fun updateAuto(manual: Boolean) {
        println("manual: $manual")
    }

    override fun updateIP(ip: String) {
        println("ip: $ip")
    }

    override fun encoderUp(){
        encoderListener?.encoderUp()
    }
    override fun encoderDown(){
        encoderListener?.encoderDown()
    }
    override fun switchOn(){
        switchListener?.switchOn()
    }
    override fun switchOff(){
        switchListener?.switchOff()
    }
    override fun registerEncoderListener(encoderListener: encoderListener){
        this.encoderListener = encoderListener
    }
    override fun registerSwitchListener(switchListener: switchListener){
        this.switchListener = switchListener
    }



}
