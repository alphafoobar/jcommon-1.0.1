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

}