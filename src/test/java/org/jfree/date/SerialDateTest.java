package org.jfree.date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SerialDateTest {

    @Test
    public void isValidWeekdayCode() throws Exception {
        assertFalse(SerialDate.isValidWeekdayCode(-11));
        assertFalse(SerialDate.isValidWeekdayCode(0));
        assertTrue(SerialDate.isValidWeekdayCode(1));
        assertTrue(SerialDate.isValidWeekdayCode(SerialDate.WEDNESDAY));
        assertTrue(SerialDate.isValidWeekdayCode(SerialDate.FRIDAY));
        assertTrue(SerialDate.isValidWeekdayCode(6));
        assertTrue(SerialDate.isValidWeekdayCode(SerialDate.SATURDAY));
        assertFalse(SerialDate.isValidWeekdayCode(13));
        assertFalse(SerialDate.isValidWeekdayCode(112));
    }

    @Test
    public void isValidMonthCode() throws Exception {
        assertFalse(SerialDate.isValidMonthCode(-11));
        assertFalse(SerialDate.isValidMonthCode(0));
        assertTrue(SerialDate.isValidMonthCode(1));
        assertTrue(SerialDate.isValidMonthCode(MonthConstants.APRIL));
        assertTrue(SerialDate.isValidMonthCode(MonthConstants.DECEMBER));
        assertFalse(SerialDate.isValidMonthCode(13));
        assertFalse(SerialDate.isValidMonthCode(112));
    }

}