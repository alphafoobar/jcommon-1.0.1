package org.jfree.date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SerialDateTest_isValidWeekInMonthCode {

    @Test
    public void isValidMonthCode() throws Exception {
        assertFalse(SerialDateUtilities.isValidWeekInMonthCode(-1));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDateImpl.LAST_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDateImpl.FIRST_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDateImpl.SECOND_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDateImpl.THIRD_WEEK_IN_MONTH));
        assertTrue(SerialDateUtilities.isValidWeekInMonthCode(SerialDateImpl.FOURTH_WEEK_IN_MONTH));
        assertFalse(SerialDateUtilities.isValidWeekInMonthCode(5));
    }

}