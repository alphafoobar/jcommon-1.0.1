package org.jfree.date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SerialDateTest {

    @Test
    public void isValidWeekdayCode() throws Exception {
        assertFalse(SerialDateUtilities.isValidWeekdayCode(-11));
        assertFalse(SerialDateUtilities.isValidWeekdayCode(0));
        assertTrue(SerialDateUtilities.isValidWeekdayCode(1));
        assertTrue(SerialDateUtilities.isValidWeekdayCode(SerialDateImpl.WEDNESDAY));
        assertTrue(SerialDateUtilities.isValidWeekdayCode(SerialDateImpl.FRIDAY));
        assertTrue(SerialDateUtilities.isValidWeekdayCode(6));
        assertTrue(SerialDateUtilities.isValidWeekdayCode(SerialDateImpl.SATURDAY));
        assertFalse(SerialDateUtilities.isValidWeekdayCode(13));
        assertFalse(SerialDateUtilities.isValidWeekdayCode(112));
    }

}