package com.vdzon.java.schaakspel

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.Test

class CalcUtilTest{
    @Test
    fun testDistance(){
        assertThat(CalcUtil.calcDistance("A1", "A1")).isCloseTo(0.0, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("A1", "A2")).isCloseTo(1.0, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("A1", "B1")).isCloseTo(1.0, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("A2", "A1")).isCloseTo(1.0, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("B1", "A1")).isCloseTo(1.0, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("A1", "B2")).isCloseTo(1.41, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("B2", "A1")).isCloseTo(1.41, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("A1", "H8")).isCloseTo(9.89, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("A8", "H1")).isCloseTo(9.89, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("H1", "A8")).isCloseTo(9.89, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("H8", "A1")).isCloseTo(9.89, Percentage.withPercentage(1.0))
        assertThat(CalcUtil.calcDistance("A1", "A21")).isCloseTo(20.00, Percentage.withPercentage(1.0))
    }
}