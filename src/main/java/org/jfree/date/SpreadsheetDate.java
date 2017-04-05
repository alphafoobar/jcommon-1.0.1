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

    private static final long serialVersionUID = -2039586705374454461L;

    /**
     * The day number (1-Jan-1900 = 2, 2-Jan-1900 = 3, ..., 31-Dec-9999 =
     * 2958465).
     */
    private int serial;

    /**
     * The day of the month (1 to 28, 29, 30 or 31 depending on the month).
     */
    private int day;

    /**
     * The month of the year (1 to 12).
     */
    private int month;

    /**
     * The year (1900 to 9999).
     */
    private int year;

    /**
     * An optional description for the date.
     */
    private String description;

    /**
     * Creates a new date instance.
     *
     * @param day the day (in the range 1 to 28/29/30/31).
     * @param month the month (in the range 1 to 12).
     * @param year the year (in the range 1900 to 9999).
     */
    public SpreadsheetDate(final int day, final int month, final int year) {

        if ((year >= 1900) && (year <= 9999)) {
            this.year = year;
        } else {
            throw new IllegalArgumentException(
                "The 'year' argument must be in range 1900 to 9999."
            );
        }

        if ((month >= MonthConstants.JANUARY) && (month <= MonthConstants.DECEMBER)) {
            this.month = month;
        } else {
            throw new IllegalArgumentException(
                "The 'month' argument must be in the range 1 to 12."
            );
        }

        if ((day >= 1) && (day <= SerialDate.lastDayOfMonth(month, year))) {
            this.day = day;
        } else {
            throw new IllegalArgumentException("Invalid 'day' argument.");
        }

        // the serial number needs to be synchronised with the day-month-year...
        this.serial = calcSerial(day, month, year);

        this.description = null;

    }

    /**
     * @param serial The serial number for the day (range: SERIAL_LOWER_BOUND to
     * SERIAL_UPPER_BOUND).
     */
    public SpreadsheetDate(int serial) {
        checkValidSerial(serial);

        this.serial = serial;

        // the day-month-year needs to be synchronised with the serial number...
        calcDayMonthYearFromSerial();
    }

    private void checkValidSerial(int serial) {
        if (serial < SERIAL_LOWER_BOUND || serial > SERIAL_UPPER_BOUND) {
            throw new IllegalArgumentException(
                "SpreadsheetDate: Serial must be in range " + SERIAL_LOWER_BOUND
                    + " to " + SERIAL_UPPER_BOUND);
        }
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
        return this.description;
    }

    /**
     * Sets the description for the date.
     *
     * @param description the description for this date (<code>null</code> permitted).
     */
    @Override
    public void setDescription(final String description) {
        this.description = description;
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
        final Calendar calendar = Calendar.getInstance();
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

    /**
     * Returns the day of the month.
     *
     * @return The day of the month.
     */
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
    public boolean isOn(SerialDate other) {
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
    public boolean isBefore(SerialDate other) {
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
    public boolean isOnOrBefore(SerialDate other) {
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
    public boolean isAfter(SerialDate other) {
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
    public boolean isOnOrAfter(SerialDate other) {
        return compareTo(other) >= 0;
    }

    /**
     * Returns <code>true</code> if this {@link SerialDate} is within the
     * specified range (INCLUSIVE).  The date order of d1 and d2 is not
     * important.
     *
     * @param d1 a boundary date for the range.
     * @param d2 the other boundary date for the range.
     * @return A boolean.
     */
    @Override
    public boolean isInRange(final SerialDate d1, final SerialDate d2) {
        return isInRange(d1, d2, SerialDate.INCLUDE_BOTH);
    }

    /**
     * Returns true if this SerialDate is within the specified range (caller
     * specifies whether or not the end-points are included).  The order of d1
     * and d2 is not important.
     *
     * @param d1 one boundary date for the range.
     * @param d2 a second boundary date for the range.
     * @param include a code that controls whether or not the start and end dates are included in
     * the range.
     * @return <code>true</code> if this SerialDate is within the specified range.
     */
    @Override
    public boolean isInRange(final SerialDate d1, final SerialDate d2,
        final int include) {
        final int s1 = d1.toSerial();
        final int s2 = d2.toSerial();
        final int start = Math.min(s1, s2);
        final int end = Math.max(s1, s2);

        final int s = toSerial();
        if (include == SerialDate.INCLUDE_BOTH) {
            return (s >= start && s <= end);
        } else if (include == SerialDate.INCLUDE_FIRST) {
            return (s >= start && s < end);
        } else if (include == SerialDate.INCLUDE_SECOND) {
            return (s > start && s <= end);
        } else {
            return (s > start && s < end);
        }
    }

    /**
     * Calculate the serial number from the day, month and year.
     * <P>
     * 1-Jan-1900 = 2.
     *
     * @param d the day.
     * @param m the month.
     * @param y the year.
     * @return the serial number from the day, month and year.
     */
    private int calcSerial(final int d, final int m, final int y) {
        final int yy = ((y - 1900) * 365) + SerialDate.leapYearCount(y - 1);
        int mm = SerialDate.AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH[m];
        if (m > MonthConstants.FEBRUARY) {
            if (SerialDate.isLeapYear(y)) {
                mm = mm + 1;
            }
        }
        return yy + mm + d + 1;
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

    /**
     * Returns a hash code for this object instance.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        return toSerial();
    }

    /**
     * Tests the equality of this date with an arbitrary object.
     * <P>
     * This method will return true ONLY if the object is an instance of the
     * {@link SerialDate} base class, and it represents the same day as this
     * {@link SpreadsheetDate}.
     *
     * @param object the object to compare (<code>null</code> permitted).
     * @return A boolean.
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof SerialDate) {
            SerialDate s = (SerialDate) object;
            return (s.toSerial() == this.toSerial());
        }
        return false;
    }

}
