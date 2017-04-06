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
 * SpreadsheetDate.java
 * --------------------
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SpreadsheetDate.java,v 1.8 2005/11/03 09:25:39 mungady Exp $
 *
 * Changes
 * -------
 * 11-Oct-2001 : Version 1 (DG);
 * 05-Nov-2001 : Added getDescription() and setDescription() methods (DG);
 * 12-Nov-2001 : Changed name from ExcelDate.java to SpreadsheetDate.java (DG);
 *               Fixed a bug in calculating day, month and year from serial 
 *               number (DG);
 * 24-Jan-2002 : Fixed a bug in calculating the serial number from the day, 
 *               month and year.  Thanks to Trevor Hills for the report (DG);
 * 29-May-2002 : Added equals(Object) method (SourceForge ID 558850) (DG);
 * 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Implemented Serializable (DG);
 * 04-Sep-2003 : Completed isInRange() methods (DG);
 * 05-Sep-2003 : Implemented Comparable (DG);
 * 21-Oct-2003 : Added hashCode() method (DG);
 *
 */

package org.jfree.date;

import java.util.Calendar;
import java.util.Date;
import javax.annotation.Nonnull;

/**
 * Represents a date using an integer, in a similar fashion to the
 * implementation in Microsoft Excel.  The range of dates supported is
 * 1-Jan-1900 to 31-Dec-9999.
 * <P>
 * Be aware that there is a deliberate bug in Excel that recognises the year
 * 1900 as a leap year when in fact it is not a leap year. You can find more
 * information on the Microsoft website in article Q181370:
 * <P>
 * http://support.microsoft.com/support/kb/articles/Q181/3/70.asp
 * <P>
 * Excel uses the convention that 1-Jan-1900 = 1.  This class uses the
 * convention 1-Jan-1900 = 2.
 * The result is that the day number in this class will be different to the
 * Excel figure for January and February 1900...but then Excel adds in an extra
 * day (29-Feb-1900 which does not actually exist!) and from that point forward
 * the day numbers will match.
 *
 * @author David Gilbert
 */
public class SpreadsheetDate extends SerialDate {
    private static final long serialVersionUID = 0L;

    private int serial;
    private int day;
    private int month;
    private int year;

    public SpreadsheetDate(int day, Month month, int year) {
        this(day, month.getMonthCode(), year);
    }

    /**
     * Creates a new date instance.
     *
     * @param day the day (in the range 1 to 28/29/30/31).
     * @param month the month (in the range 1 to 12).
     * @param year the year (in the range 1900 to 9999).
     */
    public SpreadsheetDate(int day, int month, int year) {
        validate(day, month, year);

        this.year = year;
        this.month = month;
        this.day = day;

        // the serial number needs to be synchronised with the day-month-year...
        this.serial = calcSerial(day, month, year);
    }

    public SpreadsheetDate(int serial) {
        checkValidSerial(serial);

        this.serial = serial;

        // the day-month-year needs to be synchronised with the serial number...
        calcDayMonthYearFromSerial();
    }

    /**
     * Returns the description that is attached to the date.  It is not
     * required that a date have a description, but for some applications it
     * is useful.
     *
     * @return The description that is attached to the date.
     */
    @Override
    public String getDescription() {
        return toString();
    }

    /**
     * Returns the serial number for the date, where 1 January 1900 = 2
     * (this corresponds, almost, to the numbering system used in Microsoft
     * Excel for Windows and Lotus 1-2-3).
     *
     * @return The serial number of this date.
     */
    @Override
    public int toSerial() {
        return this.serial;
    }

    /**
     * Returns a <code>java.util.Date</code> equivalent to this date.
     *
     * @return The date.
     */
    @Override
    public Date toDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(getYear(), getMonth() - 1, getDayOfMonth(), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * Returns the year (assume a valid range of 1900 to 9999).
     *
     * @return The year.
     */
    @Override
    public int getYear() {
        return this.year;
    }

    /**
     * Returns the month (January = 1, February = 2, March = 3).
     *
     * @return The month of the year.
     */
    @Override
    public int getMonth() {
        return this.month;
    }

    @Override
    public int getDayOfMonth() {
        return this.day;
    }

    /**
     * Returns a code representing the day of the week.
     * <P>
     * The codes are defined in the {@link SerialDate} class as:
     * <code>SUNDAY</code>, <code>MONDAY</code>, <code>TUESDAY</code>,
     * <code>WEDNESDAY</code>, <code>THURSDAY</code>, <code>FRIDAY</code>, and
     * <code>SATURDAY</code>.
     *
     * @return A code representing the day of the week.
     */
    @Override
    public int getDayOfWeek() {
        return (this.serial + 6) % 7 + 1;
    }

    /**
     * Implements the method required by the Comparable interface.
     *
     * @param other the other object (usually another SerialDate).
     * @return A negative integer, zero, or a positive integer as this object is less than, equal
     * to, or greater than the specified object.
     */
    @Override
    public int compareTo(@Nonnull SerialDate other) {
        return this.serial - other.toSerial();
    }

    /**
     * Returns true if this SerialDate represents the same date as the
     * specified SerialDate.
     *
     * @param other the date being compared to.
     * @return <code>true</code> if this SerialDate represents the same date as the specified
     * SerialDate.
     */
    @Override
    public boolean isOn(@Nonnull SerialDate other) {
        return compareTo(other) == 0;
    }

    /**
     * Returns true if this SerialDate represents an earlier date compared to
     * the specified SerialDate.
     *
     * @param other the date being compared to.
     * @return <code>true</code> if this SerialDate represents an earlier date compared to the
     * specified SerialDate.
     */
    @Override
    public boolean isBefore(@Nonnull SerialDate other) {
        return compareTo(other) < 0;
    }

    /**
     * Returns true if this SerialDate represents the same date as the
     * specified SerialDate.
     *
     * @param other the date being compared to.
     * @return <code>true</code> if this SerialDate represents the same date as the specified
     * SerialDate.
     */
    @Override
    public boolean isOnOrBefore(@Nonnull SerialDate other) {
        return compareTo(other) <= 0;
    }

    /**
     * Returns true if this SerialDate represents the same date as the
     * specified SerialDate.
     *
     * @param other the date being compared to.
     * @return <code>true</code> if this SerialDate represents the same date as the specified
     * SerialDate.
     */
    @Override
    public boolean isAfter(@Nonnull SerialDate other) {
        return compareTo(other) > 0;
    }

    /**
     * Returns true if this SerialDate represents the same date as the
     * specified SerialDate.
     *
     * @param other the date being compared to.
     * @return <code>true</code> if this SerialDate represents the same date as the specified
     * SerialDate.
     */
    @Override
    public boolean isOnOrAfter(@Nonnull SerialDate other) {
        return compareTo(other) >= 0;
    }

    /**
     * Returns <code>true</code> if this {@link SerialDate} is within the
     * specified range (INCLUSIVE).
     *
     * @param dateFrom a boundary date for the range.
     * @param dateTo the other boundary date for the range.
     * @return A boolean.
     */
    @Override
    public boolean isInRange(@Nonnull SerialDate dateFrom, @Nonnull SerialDate dateTo) {
        return isInRange(dateFrom, dateTo, SerialDate.INCLUDE_BOTH);
    }

    /**
     * Returns true if this SerialDate is within the specified range (caller
     * specifies whether or not the end-points are included).  The order of dateFrom
     * and dateTo is not important.
     *
     * @param dateFrom one boundary date for the range.
     * @param dateTo a second boundary date for the range.
     * @param inclusionRule a code that controls whether or not the start and end dates are included in
     * the range.
     * @return <code>true</code> if this SerialDate is within the specified range.
     */
    @Override
    public boolean isInRange(@Nonnull SerialDate dateFrom, @Nonnull SerialDate dateTo,
        int inclusionRule) {
        int start = dateFrom.toSerial();
        int end = dateTo.toSerial();

        if (inclusionRule == SerialDate.INCLUDE_BOTH) {
            return serial >= start && serial <= end;
        }

        if (inclusionRule == SerialDate.INCLUDE_FIRST) {
            return serial >= start && serial < end;
        }

        if (inclusionRule == SerialDate.INCLUDE_SECOND) {
            return serial > start && serial <= end;
        }

        return serial > start && serial < end;
    }

    /**
     * Calculate the serial number from the day, month and year.
     * <P>
     * 1-Jan-1900 = 2.
     *
     * @param day the day.
     * @param month the month.
     * @param year the year.
     * @return the serial number from the day, month and year.
     */
    private static int calcSerial(int day, int month, int year) {
        int yy = ((year - 1900) * 365) + SerialDate.leapYearCount(year - 1);
        int mm = SerialDate.AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH[month];
        if (month > Month.FEBRUARY.getMonthCode()) {
            if (SerialDate.isLeapYear(year)) {
                mm = mm + 1;
            }
        }
        return yy + mm + day + 1;
    }

    /**
     * Calculate the day, month and year from the serial number.
     */
    private void calcDayMonthYearFromSerial() {
        // get the year from the serial date
        final int days = this.serial - SERIAL_LOWER_BOUND;

        setYearFromSerialDays(days);

        final int ss2 = calcSerial(1, 1, this.year);

        int[] daysToEndOfPrecedingMonth = AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH;

        if (isLeapYear(this.year)) {
            daysToEndOfPrecedingMonth = LEAP_YEAR_AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH;
        }

        // get the month from the serial date
        int mm = 1;
        int sss = ss2 + daysToEndOfPrecedingMonth[mm] - 1;
        while (sss < this.serial) {
            mm = mm + 1;
            sss = ss2 + daysToEndOfPrecedingMonth[mm] - 1;
        }
        this.month = mm - 1;

        // what's left is d(+1);
        this.day = this.serial - ss2 - daysToEndOfPrecedingMonth[this.month] + 1;
    }

    private void setYearFromSerialDays(int days) {
        int yearIgnoringLeapYears = calculateYearIgnoringLeapYears(days);
        int yearWithOverEstimatedLeapYears = calculateYearIgnoringLeapYears(
            days - SerialDate.leapYearCount(yearIgnoringLeapYears));

        if (yearWithOverEstimatedLeapYears == yearIgnoringLeapYears) {
            this.year = yearWithOverEstimatedLeapYears;
        } else {
            int ss1 = calcSerial(1, 1, yearWithOverEstimatedLeapYears);
            while (ss1 <= this.serial) {
                yearWithOverEstimatedLeapYears = yearWithOverEstimatedLeapYears + 1;
                ss1 = calcSerial(1, 1, yearWithOverEstimatedLeapYears);
            }

            this.year = yearWithOverEstimatedLeapYears - 1;
        }
    }

    private int calculateYearIgnoringLeapYears(int days) {
        return 1900 + (days / 365);
    }

    @Override
    public int hashCode() {
        return serial;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SpreadsheetDate) {
            SpreadsheetDate s = (SpreadsheetDate) object;
            return (s.serial == serial);
        }
        return false;
    }

}
