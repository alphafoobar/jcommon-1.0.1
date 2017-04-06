/* ========================================================================
 * JCommon : a free general purpose class library for the Java(tm) platform
 * ========================================================================
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -------------------------
 * SpreadsheetDateTests.java
 * -------------------------
 * (C) Copyright 2001-2003, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SpreadsheetDateTests.java,v 1.4 2005/11/16 15:58:40 taqua Exp $
 *
 * Changes
 * -------
 * 15-Nov-2001 : Version 1 (DG);
 * 24-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 *
 */

package org.jfree.date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Tests for the SpreadsheetDate class.
 */
public class SpreadsheetDateTests extends TestCase {

    /**
     * Date representing 1 January 1900.
     */
    private SerialDate jan1Y1900 = SpreadsheetDate.createInstance(1, Month.JANUARY, 1900);

    /**
     * Date representing serial day number 2.
     */
    private SerialDate serialInstanceOf2 = SpreadsheetDate.createInstance(2);

    @Test
    public void test1Jan1900LastDayOfTheMonth() {
        SerialDate endOfTheMonth = jan1Y1900.getEndOfCurrentMonth();
        assertThat(endOfTheMonth, equalTo(SpreadsheetDate.createInstance(31, Month.JANUARY, 1900)));
    }

    @Test
    public void test7AprilLastDayOfTheMonth() {
        SerialDate endOfTheMonth = SpreadsheetDate.createInstance(7, Month.APRIL, 2017)
            .getEndOfCurrentMonth();
        assertThat(endOfTheMonth, equalTo(SpreadsheetDate.createInstance(30, Month.APRIL, 2017)));
    }

    @Test
    public void test1FebLastDayOfTheMonth() {
        SerialDate endOfTheMonth = SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000)
            .getEndOfCurrentMonth();
        assertThat(endOfTheMonth,
            equalTo(SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000)));
    }

    /**
     * 1 January 1900 is a Thursday.
     */
    public void test1Jan1900GetDayOfWeek() {
        int dayOfWeek = this.jan1Y1900.calculateDayOfWeek();
        assertEquals(SerialDateImpl.MONDAY, dayOfWeek);
    }

    /**
     * 12 November 2001 is a Monday.
     */
    public void test12Nov2001GetDayOfWeek() {
        final SerialDate nov12Y2001 = SpreadsheetDate.createInstance(12, Month.NOVEMBER, 2001);
        final int dayOfWeek = nov12Y2001.calculateDayOfWeek();
        assertEquals(SerialDateImpl.MONDAY, dayOfWeek);
    }

    /**
     * Day 2 is the first of the month.
     */
    public void testS2GetDayOfMonth() {
        final int dayOfMonth = this.serialInstanceOf2.getDayOfMonth();
        assertEquals(1, dayOfMonth);
    }

    /**
     * Day 2 is in January.
     */
    public void testS2GetMonth() {
        final int month = this.serialInstanceOf2.getMonth();
        assertEquals(Month.JANUARY.getMonthCode(), month);
    }

    /**
     * Day 2 is in 1900.
     */
    public void testS2GetYYYY() {
        int year = this.serialInstanceOf2.getYear();
        assertEquals(1900, year);
    }

    /**
     * Day 37986 is 31 Dec 2003.
     */
    public void test37986() {
        SerialDate date = SpreadsheetDate.createInstance(37986);
        checkExpectedDate(date, 31, Month.DECEMBER, 2003);
    }

    /**
     * Day 37987 is 1 Jan 2004.
     */
    public void test37987() {
        SerialDate date = SpreadsheetDate.createInstance(37987);
        checkExpectedDate(date, 1, Month.JANUARY, 2004);
    }

    /**
     * Day 38352 is 31 Dec 2004.
     */
    public void test38352() {
        SerialDate date = SpreadsheetDate.createInstance(38352);
        checkExpectedDate(date, 31, Month.DECEMBER, 2004);
    }

    /**
     * Day 38353 is 1 Jan 2005.
     */
    public void test38353() {
        SerialDate date = SpreadsheetDate.createInstance(38353);
        checkExpectedDate(date, 1, Month.JANUARY, 2005);
    }

    /**
     * Create a date for serial number 36584: it should be 28-Feb-2000.
     */
    public void test36584() {
        SerialDate date = SpreadsheetDate.createInstance(36584);
        checkExpectedDate(date, 28, Month.FEBRUARY, 2000);
    }

    /**
     * Create a date for serial number 36585: it should be 29-Feb-2000.
     */
    public void test36585() {
        SerialDate date = SpreadsheetDate.createInstance(36585);
        checkExpectedDate(date, 29, Month.FEBRUARY, 2000);
    }

    private void checkExpectedDate(SerialDate d, int expectedDay, Month expectedMonth,
        int expectedYear) {
        assertEquals(expectedDay, d.getDayOfMonth());
        assertEquals(expectedMonth.getMonthCode(), d.getMonth());
        assertEquals(expectedYear, d.getYear());
    }

    @Test
    public void testCreateFromDate() {
        Date testDate = DateUtilities.createDate(29, 2, 2004);
        SerialDate date = SpreadsheetDate.createInstance(testDate);
        checkExpectedDate(date, 29, Month.FEBRUARY, 2004);
    }


    public void test36586() {
        SerialDate date = SpreadsheetDate.createInstance(36586);
        checkExpectedDate(date, 1, Month.MARCH, 2000);
    }

    /**
     * Create a date for 01-Jan-1900: the serial number should be 2.
     */
    public void test01Jan1900ToSerial() {
        final int serial = this.jan1Y1900.toSerial();
        assertEquals(2, serial);
    }

    /**
     * Create a date for 28-Feb-1900: the serial number should be 60.
     */
    public void test28Feb1900ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(28, Month.FEBRUARY, 1900);
        assertEquals(60, date.toSerial());
    }

    /**
     * Create a date for 01-Mar-1900: the serial number should be 61.
     */
    public void test01Mar1900ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(1, Month.MARCH, 1900);
        assertEquals(61, date.toSerial());
    }

    /**
     * Create a date for 31-Dec-1999: the serial number should be 36525.
     */
    public void test31Dec1999ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(31, Month.DECEMBER, 1999);
        assertEquals(36525, date.toSerial());
    }

    /**
     * Create a date for 1-Jan-2000: the serial number should be 36526.
     */
    public void test01Jan2000ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(1, Month.JANUARY, 2000);
        assertEquals(36526, date.toSerial());
    }

    /**
     * Create a date for 31-Jan-2000: the serial number should be 36556.
     */
    public void test31Jan2000ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(31, Month.JANUARY, 2000);
        assertEquals(36556, date.toSerial());
    }

    /**
     * Create a date for 01-Feb-2000: the serial number should be 36557.
     */
    public void test01Feb2000ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000);
        assertEquals(36557, date.toSerial());
    }

    /**
     * Create a date for 28-Feb-2000: the serial number should be 36584.
     */
    public void test28Feb2000ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(28, Month.FEBRUARY, 2000);
        assertEquals(36584, date.toSerial());
    }

    /**
     * Create a date for 29-Feb-2000: the serial number should be 36585.
     */
    public void test29feb2000ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000);
        assertEquals(36585, date.toSerial());
    }

    /**
     * Create a date for 1-Mar-2000: the serial number should be 36586.
     */
    public void test1mar2000ToSerial() {
        SerialDate date = SpreadsheetDate.createInstance(1, Month.MARCH, 2000);
        assertEquals(36586, date.toSerial());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        SerialDate expectedDate = SpreadsheetDate.createInstance(15, 4, 2000);
        byte[] bytes = getBytesForSerialDate(expectedDate);

        assertThat(getSerialDateFromBytes(bytes), equalTo(expectedDate));
    }

    private SerialDate getSerialDateFromBytes(byte[] bytes)
        throws IOException, ClassNotFoundException {
        try (ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (SerialDate) in.readObject();
        }
    }

    private byte[] getBytesForSerialDate(SerialDate expectedDate) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (ObjectOutput out = new ObjectOutputStream(buffer)) {
            out.writeObject(expectedDate);
        }
        return buffer.toByteArray();
    }

}
