package org.jfree.date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SpreadsheetDateTest_isInRange {

    @Test
    public void isInRange_includeBoth() throws Exception {
        SerialDate date = SpreadsheetDate.createInstance(15, Month.FEBRUARY, 2000);

        // Positive case
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000)));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(15, Month.FEBRUARY, 2000)));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(15, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000)));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(15, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(15, Month.FEBRUARY, 2000)));

        // Negative case
        assertFalse(date.isInRange(SpreadsheetDate.createInstance(16, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000)));
        assertFalse(date.isInRange(SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(14, Month.FEBRUARY, 2000)));
    }

    @Test
    public void isInRange_includeNone() throws Exception {
        SerialDate date = SpreadsheetDate.createInstance(15, Month.MARCH, 2000);

        // Positive case
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(29, Month.MARCH, 2000), SerialDate.INCLUDE_NONE));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(16, Month.MARCH, 2000), SerialDate.INCLUDE_NONE));

        // Negative case
        assertFalse(date.isInRange(SpreadsheetDate.createInstance(15, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(29, Month.MARCH, 2000), SerialDate.INCLUDE_NONE));
        assertFalse(date.isInRange(SpreadsheetDate.createInstance(1, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(15, Month.MARCH, 2000), SerialDate.INCLUDE_NONE));
    }

    @Test
    public void isInRange_includeLeft() throws Exception {
        SerialDate date = SpreadsheetDate.createInstance(15, Month.MARCH, 2000);

        // Positive case
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(29, Month.MARCH, 2000), SerialDate.INCLUDE_FIRST));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(16, Month.MARCH, 2000), SerialDate.INCLUDE_FIRST));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(15, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(29, Month.MARCH, 2000), SerialDate.INCLUDE_FIRST));

        // Negative case
        assertFalse(date.isInRange(SpreadsheetDate.createInstance(1, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(15, Month.MARCH, 2000), SerialDate.INCLUDE_FIRST));
    }

    @Test
    public void isInRange_includeRight() throws Exception {
        SerialDate date = SpreadsheetDate.createInstance(15, Month.MARCH, 2000);

        // Positive case
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000),
            SpreadsheetDate.createInstance(29, Month.MARCH, 2000), SerialDate.INCLUDE_SECOND));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(16, Month.MARCH, 2000), SerialDate.INCLUDE_SECOND));
        assertTrue(date.isInRange(SpreadsheetDate.createInstance(1, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(15, Month.MARCH, 2000), SerialDate.INCLUDE_SECOND));

        // Negative case
        assertFalse(date.isInRange(SpreadsheetDate.createInstance(15, Month.MARCH, 2000),
            SpreadsheetDate.createInstance(29, Month.MARCH, 2000), SerialDate.INCLUDE_SECOND));
    }

}