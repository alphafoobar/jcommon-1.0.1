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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SerialDateTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private SerialDate nov9Y2001 = SpreadsheetDate.createInstance(9, Month.NOVEMBER, 2001);

    @Test
    public void testAddMonthsTo9Nov2001() {
        final SerialDate jan9Y2002 = nov9Y2001.addMonths(2);
        final SerialDate answer = SpreadsheetDate.createInstance(9, 1, 2002);
        assertEquals(answer, jan9Y2002);
    }

    @Test
    public void testAddMonthsTo5Oct2003() {
        SerialDate d1 = SpreadsheetDate.createInstance(5, Month.OCTOBER, 2003);
        assertThat(d1.addMonths(2),
            equalTo(SpreadsheetDate.createInstance(5, Month.DECEMBER, 2003)));
    }

    @Test
    public void testAddMonthsTo1Jan2003() {
        SerialDate d1 = SpreadsheetDate.createInstance(1, Month.JANUARY, 2003);
        assertThat(d1.addMonths(0), equalTo(d1));
    }

    @Test
    public void testMondayPrecedingFriday9Nov2001() {
        SerialDate before = this.nov9Y2001.getPreviousDayOfWeek(SpreadsheetDate.MONDAY);
        assertEquals(5, before.getDayOfMonth());
    }

    @Test
    public void testTuesdayPrecedingFriday9Nov2001() {
        SerialDate before = this.nov9Y2001.getPreviousDayOfWeek(SpreadsheetDate.TUESDAY);
        assertEquals(6, before.getDayOfMonth());
    }

    @Test
    public void testWednesdayPrecedingFriday9Nov2001() {
        SerialDate before = this.nov9Y2001.getPreviousDayOfWeek(SpreadsheetDate.WEDNESDAY);
        assertEquals(7, before.getDayOfMonth());
    }

    @Test
    public void testThursdayPrecedingFriday9Nov2001() {
        SerialDate before = this.nov9Y2001.getPreviousDayOfWeek(SpreadsheetDate.THURSDAY);
        assertEquals(8, before.getDayOfMonth());
    }

    @Test
    public void testFridayPrecedingFriday9Nov2001() {
        SerialDate before = this.nov9Y2001.getPreviousDayOfWeek(SpreadsheetDate.FRIDAY);
        assertEquals(2, before.getDayOfMonth());
    }

    @Test
    public void testSaturdayPrecedingFriday9Nov2001() {
        SerialDate before = this.nov9Y2001.getPreviousDayOfWeek(SpreadsheetDate.SATURDAY);
        assertEquals(3, before.getDayOfMonth());
    }

    @Test
    public void testSundayPrecedingFriday9Nov2001() {
        SerialDate before = this.nov9Y2001.getPreviousDayOfWeek(SpreadsheetDate.SUNDAY);
        assertEquals(4, before.getDayOfMonth());
    }

    @Test
    public void testFridayFollowingFriday9Nov2001() {
        SerialDate after = nov9Y2001.getFollowingDayOfWeek(SpreadsheetDate.FRIDAY);
        assertEquals(16, after.getDayOfMonth());
    }

    @Test
    public void testSaturdayFollowingFriday9Nov2001() {
        SerialDate after = nov9Y2001.getFollowingDayOfWeek(SpreadsheetDate.SATURDAY);
        assertEquals(10, after.getDayOfMonth());
    }

    @Test
    public void testSundayFollowingFriday9Nov2001() {
        SerialDate after = nov9Y2001.getFollowingDayOfWeek(SpreadsheetDate.SUNDAY);
        assertEquals(11, after.getDayOfMonth());
    }

    @Test
    public void testMondayFollowingFriday9Nov2001() {
        SerialDate mondayAfter = this.nov9Y2001.getFollowingDayOfWeek(SpreadsheetDate.MONDAY);
        assertEquals(12, mondayAfter.getDayOfMonth());
    }

    @Test
    public void testTuesdayFollowingFriday9Nov2001() {
        SerialDate after = nov9Y2001.getFollowingDayOfWeek(SpreadsheetDate.TUESDAY);
        assertEquals(13, after.getDayOfMonth());
    }

    @Test
    public void testWednesdayFollowingFriday9Nov2001() {
        SerialDate after = nov9Y2001.getFollowingDayOfWeek(SpreadsheetDate.WEDNESDAY);
        assertEquals(14, after.getDayOfMonth());
    }

    @Test
    public void testThursdayFollowingFriday9Nov2001() {
        SerialDate after = nov9Y2001.getFollowingDayOfWeek(SpreadsheetDate.THURSDAY);
        assertEquals(15, after.getDayOfMonth());
    }

    @Test
    public void testMondayNearestFriday9Nov2001() {
        SerialDate mondayNearest = this.nov9Y2001.getNearestDayOfWeek(
            SpreadsheetDate.MONDAY
        );
        assertEquals(12, mondayNearest.getDayOfMonth());
    }

    @Test
    public void testMondayNearest22Jan1970() {
        SerialDate jan22Y1970 = SpreadsheetDate.createInstance(22, Month.JANUARY, 1970);
        SerialDate mondayNearest = jan22Y1970
            .getNearestDayOfWeek(SpreadsheetDate.MONDAY);
        assertEquals(19, mondayNearest.getDayOfMonth());
    }

    @Test
    public void testWeekdayCodeToString() {
        final String test = SerialDateUtilities.weekdayCodeToString(SpreadsheetDate.SATURDAY);
        assertEquals("Saturday", test);

    }

    @Test
    public void testStringToWeekday_Wednesday() {
        int weekday = SerialDateUtilities.stringToWeekdayCode("Wednesday");
        assertEquals(SpreadsheetDate.WEDNESDAY, weekday);
        weekday = SerialDateUtilities.stringToWeekdayCode(" Wednesday ");
        assertEquals(SpreadsheetDate.WEDNESDAY, weekday);
        weekday = SerialDateUtilities.stringToWeekdayCode("Wed");
        assertEquals(SpreadsheetDate.WEDNESDAY, weekday);
    }

    @Test
    public void testStringToWeekday_Monday() {
        assertEquals(SpreadsheetDate.MONDAY, SerialDateUtilities.stringToWeekdayCode("Monday"));
        assertEquals(SpreadsheetDate.MONDAY, SerialDateUtilities.stringToWeekdayCode("Mon"));
    }

    @Test
    public void testStringToWeekday_Tuesday() {
        assertEquals(SpreadsheetDate.TUESDAY, SerialDateUtilities.stringToWeekdayCode("Tuesday"));
        assertEquals(SpreadsheetDate.TUESDAY, SerialDateUtilities.stringToWeekdayCode("Tue"));
    }

    @Test
    public void testStringToWeekday_Thursday() {
        assertEquals(SpreadsheetDate.THURSDAY, SerialDateUtilities.stringToWeekdayCode("Thursday"));
        assertEquals(SpreadsheetDate.THURSDAY, SerialDateUtilities.stringToWeekdayCode("Thu"));
    }

    @Test
    public void testStringToWeekday_Sunday() {
        assertEquals(SpreadsheetDate.SUNDAY, SerialDateUtilities.stringToWeekdayCode("Sunday"));
        assertEquals(SpreadsheetDate.SUNDAY, SerialDateUtilities.stringToWeekdayCode("Sun"));
    }

    @Test
    public void testStringToMonthCode() {
        int m = SerialDateUtilities.stringToMonthCode("January");
        assertEquals(Month.JANUARY.getMonthCode(), m);

        m = SerialDateUtilities.stringToMonthCode(" January ");
        assertEquals(Month.JANUARY.getMonthCode(), m);

        m = SerialDateUtilities.stringToMonthCode("Jan");
        assertEquals(Month.JANUARY.getMonthCode(), m);
    }

    @Test
    public void testMonthCodeToStringCode() {
        assertEquals("December", Month.DECEMBER.getLongName());
    }

    @Test
    public void testMonthOctoberGetShort() {
        assertEquals("Jan", Month.JANUARY.getShortName());
    }

    @Test
    public void testIsNotLeapYear1900() {
        assertFalse(SerialDateUtilities.isLeapYear(1900));
    }

    @Test
    public void testIsNotLeapYear1800() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The 'year' must be in range");
        SerialDateUtilities.isLeapYear(1800);
    }

    @Test
    public void testIsNotLeapYear2100() {
        assertFalse(SerialDateUtilities.isLeapYear(2100));
    }

    @Test
    public void testIsNotLeapYear2200() {
        assertFalse(SerialDateUtilities.isLeapYear(2200));
    }

    @Test
    public void testIsNotLeapYear2300() {
        assertFalse(SerialDateUtilities.isLeapYear(2300));
    }

    @Test
    public void testIsLeapYear2400() {
        assertTrue(SerialDateUtilities.isLeapYear(2400));
    }

    @Test
    public void testIsLeapYear2000() {
        assertTrue(SerialDateUtilities.isLeapYear(2000));
    }

    @Test
    public void testIsLeapYear1992() {
        assertTrue(SerialDateUtilities.isLeapYear(1992));
    }

    @Test
    public void testIsLeapYear2004() {
        assertTrue(SerialDateUtilities.isLeapYear(2004));
    }

    @Test
    public void testIsLeapYear2008() {
        assertTrue(SerialDateUtilities.isLeapYear(2008));
    }

    @Test
    public void testLeapYearCount1899() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The 'year' must be in range 1900 to");

        SerialDateUtilities.leapYearCount(1899);
    }

    @Test
    public void testLeapYearCount1900() {
        assertThat(SerialDateUtilities.leapYearCount(1900), equalTo(0));
    }

    @Test
    public void testLeapYearCount1903() {
        assertThat(SerialDateUtilities.leapYearCount(1903), equalTo(0));
    }

    @Test
    public void testLeapYearCount1904() {
        assertThat(SerialDateUtilities.leapYearCount(1904), equalTo(1));
    }

    @Test
    public void testLeapYearCount1999() {
        assertThat(SerialDateUtilities.leapYearCount(1999), equalTo(24));
    }

    @Test
    public void testLeapYearCount2000() {
        assertThat(SerialDateUtilities.leapYearCount(2000), equalTo(25));
    }

    @Test
    public void testLeapYearCount2017() {
        assertThat(SerialDateUtilities.leapYearCount(2017), equalTo(29));
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

    @Test
    public void testAddAYear() {
        SerialDate serialDate = SpreadsheetDate.createInstance(29, 2, 2004).addYears(1);
        SerialDate expected = SpreadsheetDate.createInstance(28, 2, 2005);
        assertTrue(serialDate.isOn(expected));
        assertThat(serialDate, equalTo(expected));
    }

    @Test
    public void testAdd0Years() {
        SerialDate serialDate = SpreadsheetDate.createInstance(29, 2, 2004).addYears(0);
        SerialDate expected = SpreadsheetDate.createInstance(29, 2, 2004);
        assertThat(serialDate, equalTo(expected));
        assertTrue(serialDate.isOn(expected));
    }

    @Test
    public void testAdd12Years() {
        SerialDate serialDate = SpreadsheetDate.createInstance(29, 2, 2004).addYears(12);
        SerialDate expected = SpreadsheetDate.createInstance(29, 2, 2016);
        assertThat(serialDate, equalTo(expected));
        assertTrue(serialDate.isOn(expected));
    }

    @Test
    public void testAdd2Years() {
        SerialDate serialDate = SpreadsheetDate.createInstance(29, 2, 2004).addYears(2);
        SerialDate expected = SpreadsheetDate.createInstance(28, 2, 2006);
        assertTrue(serialDate.isOn(expected));
        assertThat(serialDate, equalTo(expected));
    }

    @Test
    public void testSubtract104Years() {
        SerialDate serialDate = SpreadsheetDate.createInstance(29, 2, 2004).addYears(-104);
        SerialDate expected = SpreadsheetDate.createInstance(28, 2, 1900);
        assertTrue(serialDate.isOn(expected));
        assertThat(serialDate, equalTo(expected));
    }

    @Test
    public void testSubtract5Years() {
        SerialDate serialDate = SpreadsheetDate.createInstance(29, 2, 2004).addYears(-5);
        SerialDate expected = SpreadsheetDate.createInstance(28, 2, 1999);
        assertTrue(serialDate.isOn(expected));
        assertThat(serialDate, equalTo(expected));
    }

    @Test
    public void testAddMonths() {
        SerialDate d1 = SpreadsheetDate.createInstance(31, 5, 2004);

        SerialDate d2 = d1.addMonths(1);
        assertEquals(30, d2.getDayOfMonth());
        assertEquals(6, d2.getMonth());
        assertEquals(2004, d2.getYear());

        SerialDate d3 = d1.addMonths(2);
        assertEquals(31, d3.getDayOfMonth());
        assertEquals(7, d3.getMonth());
        assertEquals(2004, d3.getYear());

        SerialDate d4 = d1.addMonths(1).addMonths(1);
        assertEquals(30, d4.getDayOfMonth());
        assertEquals(7, d4.getMonth());
        assertEquals(2004, d4.getYear());
    }
}
