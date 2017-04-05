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
 * --------------------
 * SerialDateTests.java
 * --------------------
 * (C) Copyright 2001-2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SerialDateTests.java,v 1.6 2005/11/16 15:58:40 taqua Exp $
 *
 * Changes
 * -------
 * 15-Nov-2001 : Version 1 (DG);
 * 25-Jun-2002 : Removed unnecessary import (DG);
 * 24-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Added serialization test (DG);
 * 05-Jan-2005 : Added test for bug report 1096282 (DG);
 *
 */

package org.jfree.date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import org.junit.Test;

public class SerialDateTests {

    private SerialDate nov9Y2001 = SerialDate.createInstance(9, MonthConstants.NOVEMBER, 2001);

    @Test
    public void testAddMonthsTo9Nov2001() {
        final SerialDate jan9Y2002 = SerialDate.addMonths(2, this.nov9Y2001);
        final SerialDate answer = SerialDate.createInstance(9, 1, 2002);
        assertEquals(answer, jan9Y2002);
    }

    @Test
    public void testAddMonthsTo5Oct2003() {
        final SerialDate d1 = SerialDate.createInstance(5, MonthConstants.OCTOBER, 2003);
        final SerialDate d2 = SerialDate.addMonths(2, d1);
        assertEquals(d2, SerialDate.createInstance(5, MonthConstants.DECEMBER, 2003));
    }

    @Test
    public void testAddMonthsTo1Jan2003() {
        final SerialDate d1 = SerialDate.createInstance(1, MonthConstants.JANUARY, 2003);
        final SerialDate d2 = SerialDate.addMonths(0, d1);
        assertEquals(d2, d1);
    }

    @Test
    public void testMondayPrecedingFriday9Nov2001() {
        SerialDate mondayBefore = SerialDate.getPreviousDayOfWeek(
            SerialDate.MONDAY, this.nov9Y2001
        );
        assertEquals(5, mondayBefore.getDayOfMonth());
    }

    @Test
    public void testMondayFollowingFriday9Nov2001() {
        SerialDate mondayAfter = SerialDate.getFollowingDayOfWeek(
            SerialDate.MONDAY, this.nov9Y2001
        );
        assertEquals(12, mondayAfter.getDayOfMonth());
    }

    @Test
    public void testMondayNearestFriday9Nov2001() {
        SerialDate mondayNearest = SerialDate.getNearestDayOfWeek(
            SerialDate.MONDAY, this.nov9Y2001
        );
        assertEquals(12, mondayNearest.getDayOfMonth());
    }

    @Test
    public void testMondayNearest22Jan1970() {
        SerialDate jan22Y1970 = SerialDate.createInstance(22, MonthConstants.JANUARY, 1970);
        SerialDate mondayNearest = SerialDate.getNearestDayOfWeek(SerialDate.MONDAY, jan22Y1970);
        assertEquals(19, mondayNearest.getDayOfMonth());
    }

    @Test
    public void testWeekdayCodeToString() {
        final String test = SerialDate.weekdayCodeToString(SerialDate.SATURDAY);
        assertEquals("Saturday", test);

    }

    @Test
    public void testStringToWeekday_Wednesday() {
        int weekday = SerialDate.stringToWeekdayCode("Wednesday");
        assertEquals(SerialDate.WEDNESDAY, weekday);
        weekday = SerialDate.stringToWeekdayCode(" Wednesday ");
        assertEquals(SerialDate.WEDNESDAY, weekday);
        weekday = SerialDate.stringToWeekdayCode("Wed");
        assertEquals(SerialDate.WEDNESDAY, weekday);
    }

    @Test
    public void testStringToWeekday_Monday() {
        assertEquals(SerialDate.MONDAY, SerialDate.stringToWeekdayCode("Monday"));
        assertEquals(SerialDate.MONDAY, SerialDate.stringToWeekdayCode("Mon"));
    }

    @Test
    public void testStringToWeekday_Tuesday() {
        assertEquals(SerialDate.TUESDAY, SerialDate.stringToWeekdayCode("Tuesday"));
        assertEquals(SerialDate.TUESDAY, SerialDate.stringToWeekdayCode("Tue"));
    }

    @Test
    public void testStringToWeekday_Thursday() {
        assertEquals(SerialDate.THURSDAY, SerialDate.stringToWeekdayCode("Thursday"));
        assertEquals(SerialDate.THURSDAY, SerialDate.stringToWeekdayCode("Thu"));
    }

    @Test
    public void testStringToWeekday_Sunday() {
        assertEquals(SerialDate.SUNDAY, SerialDate.stringToWeekdayCode("Sunday"));
        assertEquals(SerialDate.SUNDAY, SerialDate.stringToWeekdayCode("Sun"));
    }

    @Test
    public void testStringToMonthCode() {

        int m = SerialDate.stringToMonthCode("January");
        assertEquals(MonthConstants.JANUARY, m);

        m = SerialDate.stringToMonthCode(" January ");
        assertEquals(MonthConstants.JANUARY, m);

        m = SerialDate.stringToMonthCode("Jan");
        assertEquals(MonthConstants.JANUARY, m);

    }

    @Test
    public void testMonthCodeToStringCode() {
        String test = SerialDate.monthCodeToString(MonthConstants.DECEMBER);
        assertEquals("December", test);
    }

    @Test
    public void testIsNotLeapYear1900() {
        assertFalse(SerialDate.isLeapYear(1900));
    }

    @Test
    public void testIsLeapYear2000() {
        assertTrue(SerialDate.isLeapYear(2000));
    }

    @Test
    public void testLeapYearCount1899() {
        assertThat(SerialDate.leapYearCount(1899), equalTo(0));
    }

    @Test
    public void testLeapYearCount1903() {
        assertThat(SerialDate.leapYearCount(1903), equalTo(0));
    }

    @Test
    public void testLeapYearCount1904() {
        assertThat(SerialDate.leapYearCount(1904), equalTo(1));
    }

    @Test
    public void testLeapYearCount1999() {
        assertThat(SerialDate.leapYearCount(1999), equalTo(24));
    }

    @Test
    public void testLeapYearCount2000() {
        assertThat(SerialDate.leapYearCount(2000), equalTo(25));
    }

    @Test
    public void testLeapYearCount2017() {
        assertThat(SerialDate.leapYearCount(2017), equalTo(29));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        SerialDate expectedDate = SerialDate.createInstance(15, 4, 2000);
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

    @Test
    public void testSerial1096282() {
        SerialDate d = SerialDate.createInstance(29, 2, 2004);
        d = SerialDate.addYears(1, d);
        SerialDate expected = SerialDate.createInstance(28, 2, 2005);
        assertTrue(d.isOn(expected));
    }

    @Test
    public void testAddMonths() {
        SerialDate d1 = SerialDate.createInstance(31, 5, 2004);

        SerialDate d2 = SerialDate.addMonths(1, d1);
        assertEquals(30, d2.getDayOfMonth());
        assertEquals(6, d2.getMonth());
        assertEquals(2004, d2.getYear());

        SerialDate d3 = SerialDate.addMonths(2, d1);
        assertEquals(31, d3.getDayOfMonth());
        assertEquals(7, d3.getMonth());
        assertEquals(2004, d3.getYear());

        SerialDate d4 = SerialDate.addMonths(1, SerialDate.addMonths(1, d1));
        assertEquals(30, d4.getDayOfMonth());
        assertEquals(7, d4.getMonth());
        assertEquals(2004, d4.getYear());
    }
}
