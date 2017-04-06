package org.jfree.date;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SpreadsheetDateTest {

    @Test
    public void isOn() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertTrue(date.isOn(date));
        assertTrue(date.isOn(SpreadsheetDate.createInstance(5, Month.APRIL, 2017)));
    }

    @Test
    public void isOn_whenBefore() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertFalse(date.isOn(SpreadsheetDate.createInstance(4, Month.APRIL, 2017)));
    }

    @Test
    public void isOn_whenAfter() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertFalse(date.isOn(SpreadsheetDate.createInstance(6, Month.APRIL, 2017)));
    }

    @Test
    public void isBefore() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertTrue(date.isBefore(SpreadsheetDate.createInstance(6, Month.APRIL, 2017)));
    }

    @Test
    public void isBefore_whenAfter() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertFalse(date.isBefore(SpreadsheetDate.createInstance(4, Month.APRIL, 2017)));
    }

    @Test
    public void isBefore_whenOn() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertFalse(date.isBefore(SpreadsheetDate.createInstance(5, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrBefore(){
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertTrue(date.isOnOrBefore(SpreadsheetDate.createInstance(5, Month.APRIL, 2017)));
        assertTrue(date.isOnOrBefore(SpreadsheetDate.createInstance(6, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrBefore_whenAfter(){
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertTrue(date.isOnOrBefore(SpreadsheetDate.createInstance(6, Month.APRIL, 2017)));
    }

    @Test
    public void isAfter() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertTrue(date.isAfter(SpreadsheetDate.createInstance(4, Month.APRIL, 2017)));
    }

    @Test
    public void isAfter_whenOn() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertFalse(date.isAfter(SpreadsheetDate.createInstance(5, Month.APRIL, 2017)));
    }

    @Test
    public void isAfter_whenBefore() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertFalse(date.isAfter(SpreadsheetDate.createInstance(6, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrAfter() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertTrue(date.isOnOrAfter(SpreadsheetDate.createInstance(4, Month.APRIL, 2017)));
        assertTrue(date.isOnOrAfter(SpreadsheetDate.createInstance(5, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrAfter_whenBefore() {
        SerialDate date = SpreadsheetDate.createInstance(5, Month.APRIL, 2017);
        assertFalse(date.isOnOrAfter(SpreadsheetDate.createInstance(6, Month.APRIL, 2017)));
    }

}