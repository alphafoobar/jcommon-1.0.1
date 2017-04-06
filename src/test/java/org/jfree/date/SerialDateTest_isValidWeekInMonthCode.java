package org.jfree.date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SerialDateTest_isValidWeekInMonthCode {

    @Test
    public void isValidMonthCode() throws Exception {
        assertFalse(SerialDateUtilities.isValidWeekInMonthCode(-1));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDate.LAST_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDate.FIRST_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDate.SECOND_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDate.THIRD_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDate.FOURTH_WEEK_IN_MONTH));
        assertFalse(SerialDateUtilities.isValidWeekInMonthCode(5));
    }

}