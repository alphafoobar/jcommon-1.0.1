package org.jfree.date;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SpreadsheetDateTest {

    @Test
    public void isOn() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertTrue(date.isOn(date));
        assertTrue(date.isOn(new SpreadsheetDate(5, Month.APRIL, 2017)));
    }

    @Test
    public void isOn_whenBefore() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertFalse(date.isOn(new SpreadsheetDate(4, Month.APRIL, 2017)));
    }

    @Test
    public void isOn_whenAfter() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertFalse(date.isOn(new SpreadsheetDate(6, Month.APRIL, 2017)));
    }

    @Test
    public void isBefore() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertTrue(date.isBefore(new SpreadsheetDate(6, Month.APRIL, 2017)));
    }

    @Test
    public void isBefore_whenAfter() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertFalse(date.isBefore(new SpreadsheetDate(4, Month.APRIL, 2017)));
    }

    @Test
    public void isBefore_whenOn() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertFalse(date.isBefore(new SpreadsheetDate(5, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrBefore(){
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertTrue(date.isOnOrBefore(new SpreadsheetDate(5, Month.APRIL, 2017)));
        assertTrue(date.isOnOrBefore(new SpreadsheetDate(6, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrBefore_whenAfter(){
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertTrue(date.isOnOrBefore(new SpreadsheetDate(6, Month.APRIL, 2017)));
    }

    @Test
    public void isAfter() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertTrue(date.isAfter(new SpreadsheetDate(4, Month.APRIL, 2017)));
    }

    @Test
    public void isAfter_whenOn() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertFalse(date.isAfter(new SpreadsheetDate(5, Month.APRIL, 2017)));
    }

    @Test
    public void isAfter_whenBefore() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertFalse(date.isAfter(new SpreadsheetDate(6, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrAfter() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertTrue(date.isOnOrAfter(new SpreadsheetDate(4, Month.APRIL, 2017)));
        assertTrue(date.isOnOrAfter(new SpreadsheetDate(5, Month.APRIL, 2017)));
    }

    @Test
    public void isOnOrAfter_whenBefore() {
        SpreadsheetDate date = new SpreadsheetDate(5, Month.APRIL, 2017);
        assertFalse(date.isOnOrAfter(new SpreadsheetDate(6, Month.APRIL, 2017)));
    }

}