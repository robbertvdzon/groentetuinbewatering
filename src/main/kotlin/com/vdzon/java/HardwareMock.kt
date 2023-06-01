package com.vdzon.java


class HardwareMock : Hardware {
    private var encoderListener: EncoderListener? = null
    private var switchListener: SwitchListener? = null
    private var klepListener: KlepListener? = null


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
    override fun updateKlepState(klepState: String){
        println("klepState: $klepState")
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
    override fun registerEncoderListener(encoderListener: EncoderListener){
        this.encoderListener = encoderListener
    }
    override fun registerSwitchListener(switchListener: SwitchListener){
        this.switchListener = switchListener
    }
    override fun registerKlepListener(klepListener: KlepListener){
        this.klepListener = klepListener
    }




}
