package org.jfree.date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MonthTest {

    @Test
    public void calculateCalendarMonthCode() throws Exception {
        assertThat(Month.JANUARY.getCalendarMonthCode(), equalTo(0));
        assertThat(Month.APRIL.getCalendarMonthCode(), equalTo(3));
    }

    @Test
    public void fromCalendarMonthCode() throws Exception {
        assertThat(Month.fromCalendarMonthCode(3), equalTo(Month.APRIL));
    }

    @Test
    public void isValidMonthCode() throws Exception {
        assertFalse(Month.isValidMonth(-11));
        assertFalse(Month.isValidMonth(0));
        assertTrue(Month.isValidMonth(1));
        assertTrue(Month.isValidMonth(3));
        assertTrue(Month.isValidMonth(12));
        assertFalse(Month.isValidMonth(13));
        assertFalse(Month.isValidMonth(112));
    }

    @Test
    public void monthLongName() {
        assertThat(Month.fromMonthCode(4), equalTo(Month.APRIL));
    }

    @Test
    public void monthCodeToLongName() {
        assertThat(Month.monthCodeToLongName(4), equalTo("April"));
    }

    @Test
    public void monthCodeToShortName() {
        assertThat(Month.monthCodeToShortName(3), equalTo("Mar"));
    }
}