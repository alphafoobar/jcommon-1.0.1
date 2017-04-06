package org.jfree.date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SerialDateTest_quarterForMonth {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void validateMonths_negativeIsNotValid() {
        checkMonthsThatAreNotValid(-1);
    }

    @Test
    public void validateMonths_zeroIsNotValid() {
        checkMonthsThatAreNotValid(0);
    }

    @Test
    public void validateMonths_greaterThan12IsNotValid() {
        checkMonthsThatAreNotValid(13);
    }

    private void checkMonthsThatAreNotValid(int code) {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The 'month' must be in the range 1 to 12");

        Month.monthCodeToQuarter(code);
    }

    @Test
    public void monthCodeToFirstQuarter() throws Exception {
        checkMonthsInQuarter(equalTo(1), Month.JANUARY, Month.FEBRUARY,
            Month.MARCH);
    }

    @Test
    public void monthCodeToSecondQuarter() throws Exception {
        checkMonthsInQuarter(equalTo(2), Month.APRIL, Month.MAY,
            Month.JUNE);
    }

    @Test
    public void monthCodeToThirdQuarter() throws Exception {
        checkMonthsInQuarter(equalTo(3), Month.JULY, Month.AUGUST,
            Month.SEPTEMBER);
    }

    @Test
    public void monthCodeToFourthQuarter() throws Exception {
        checkMonthsInQuarter(equalTo(4), Month.OCTOBER, Month.NOVEMBER,
            Month.DECEMBER);
    }

    private void checkMonthsInQuarter(Matcher<Integer> matcher, Month... tests) {
        for (Month test : tests) {
            assertThat(Month.monthCodeToQuarter(test.getMonthCode()), matcher);
        }
    }

}