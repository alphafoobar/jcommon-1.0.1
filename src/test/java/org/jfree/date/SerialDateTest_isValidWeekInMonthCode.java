package org.jfree.date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SerialDateTest_isValidWeekInMonthCode {

    @Test
    public void isValidMonthCode() throws Exception {
        assertFalse(SerialDate.isValidWeekInMonthCode(-1));
        assertTrue(SerialDate.isValidWeekInMonthCode(SerialDate.LAST_WEEK_IN_MONTH));
        assertTrue(SerialDate.isValidWeekInMonthCode(SerialDate.FIRST_WEEK_IN_MONTH));
        assertTrue(SerialDate.isValidWeekInMonthCode(SerialDate.SECOND_WEEK_IN_MONTH));
        assertTrue(SerialDate.isValidWeekInMonthCode(SerialDate.THIRD_WEEK_IN_MONTH));
        assertTrue(SerialDate.isValidWeekInMonthCode(SerialDate.FOURTH_WEEK_IN_MONTH));
        assertFalse(SerialDate.isValidWeekInMonthCode(5));
    }

}