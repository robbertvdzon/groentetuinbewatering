package com.vdzon.java

import com.vdzon.java.ui.MainWeb
import org.slf4j.LoggerFactory
import java.util.*
import java.util.stream.Collectors

object BerekenVersnelling {
    /*
  100: 65
  1000: 335
  2000: 525
  10.000 1576
  15.000 2201
   */
    private val log = LoggerFactory.getLogger(BerekenVersnelling::class.java)
    const val MAX_SNELHEID = 1000000 / 40 // pulsen per sec
            .toDouble()
    const val VERSNELLINGSTIJD = 200000.0 // in microsec
    const val INDEX_STEPS = 20
    private const val CALCULATION_PROCESSOR_TIME = 45

    @JvmStatic
    fun main(args: Array<String>) {
        val snelheidList = getSnelheidList().stream().map { i: Int -> i.toString() }.collect(Collectors.toList())
        log.info("static const int delayList[] = {" + java.lang.String.join(",", snelheidList) + "};")
        log.info("static const int delayArraySize = " + snelheidList.size + ";")
        log.info("static const int indexSteps = $INDEX_STEPS;")
        log.info("10000:" + berekenTijd(10000))
        log.info("20000:" + berekenTijd(20000))
        val delays = calcDelays(10000, 20000)
        log.info("delay 2=" + delays.delay2)
        log.info("delay 3=" + delays.delay3)
        log.info("10000:" + berekenTijd(10000, delays.delay2, CALCULATION_PROCESSOR_TIME))
        log.info("20000:" + berekenTijd(20000, delays.delay3, CALCULATION_PROCESSOR_TIME))
    }

    fun calcDelays(pulses1: Int, pulses2: Int): Delays {
        val tijd1 = berekenTijd(pulses1)
        val tijd2 = berekenTijd(pulses2)
        var tijd = Math.max(tijd1, tijd2)
        if (tijd < 100) {
            tijd = 100.0
        }
        val doubleDiff1 = tijd - tijd1
        val doubleDiff2 = tijd - tijd2
        val sleepTijd1 = berekenTijd(pulses1, 100.0, 0)
        val sleepTijd2 = berekenTijd(pulses2, 100.0, 0)
        val newSleepTime1 = sleepTijd1 + doubleDiff1
        val newSleepTime2 = sleepTijd2 + doubleDiff2
        val delayFactor1: Double = if (pulses1 == 0) 100.0 else 100 * newSleepTime1 / sleepTijd1
        val delayFactor2: Double = if (pulses2 == 0) 100.0 else 100 * newSleepTime2 / sleepTijd2
        val delays = Delays()
        delays.delay1 = delayFactor1
        delays.delay2 = delayFactor2
        delays.totalTime = tijd.toLong()
        return delays
    }

    @JvmOverloads
    fun berekenTijd(totalSteps: Int, vertraging: Double = 100.0, calculationProcessorTime: Int = CALCULATION_PROCESSOR_TIME): Double {
        val snelheidList = getSnelheidList()
        var time: Long = 0
        val halfway = totalSteps / 2.toLong()
        var delayIndex = 0
        var remainingDelayIndex = 0
        var delay = 0
        val delayArraySize = snelheidList.size
        for (i in 0 until totalSteps) {
            val remainingSteps = totalSteps - i
            delayIndex = i / INDEX_STEPS
            remainingDelayIndex = remainingSteps / INDEX_STEPS
            if (i == 0 || i % INDEX_STEPS == 0) {
                if (i < halfway && delayIndex < delayArraySize) delay = snelheidList[delayIndex]
                if (i > halfway && remainingDelayIndex < delayArraySize) delay = snelheidList[remainingDelayIndex]
            }
            var tmpDelay = delay.toDouble()
            tmpDelay *= vertraging / 100
            val tmpDelayInt = tmpDelay.toInt()
            time += tmpDelayInt * 2 + calculationProcessorTime.toLong()
        }
        return (time / 1000).toDouble()
    }

    fun getSnelheidList(): List<Int> {
        val result: MutableList<Int> = ArrayList()
        var count = 0
        var finished = false
        var tijd = 0.001
        while (!finished) {
            val delay = VERSNELLINGSTIJD / (MAX_SNELHEID * tijd)
            val snelheid = 1000000 / delay
            finished = snelheid > MAX_SNELHEID
            tijd += delay / 1000000
            count++
            if (count % INDEX_STEPS == 0) {
                result.add(delay.toInt())
            }
        }
        return result
    }
}
