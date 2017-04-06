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
 * ---------------
 * SerialDate.java
 * ---------------
 * (C) Copyright 2001-2005, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SerialDate.java,v 1.7 2005/11/03 09:25:17 mungady Exp $
 *
 * Changes (from 11-Oct-2001)
 * --------------------------
 * 11-Oct-2001 : Re-organised the class and moved it to new package 
 *               com.jrefinery.date (DG);
 * 05-Nov-2001 : Added a getDescription() method, and eliminated NotableDate 
 *               class (DG);
 * 12-Nov-2001 : IBD requires setDescription() method, now that NotableDate 
 *               class is gone (DG);  Changed getPreviousDayOfWeek(), 
 *               getFollowingDayOfWeek() and getNearestDayOfWeek() to correct 
 *               bugs (DG);
 * 05-Dec-2001 : Fixed bug in SpreadsheetDate class (DG);
 * 29-May-2002 : Moved the month constants into a separate interface 
 *               (MonthConstants) (DG);
 * 27-Aug-2002 : Fixed bug in addMonths() method, thanks to N???levka Petr (DG);
 * 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 13-Mar-2003 : Implemented Serializable (DG);
 * 29-May-2003 : Fixed bug in addMonths method (DG);
 * 04-Sep-2003 : Implemented Comparable.  Updated the isInRange javadocs (DG);
 * 05-Jan-2005 : Fixed bug in addYears() method (1096282) (DG);
 * 
 */

package org.jfree.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * An abstract class that defines our requirements for manipulating dates,
 * without tying down a particular implementation.
 * <P>
 * Requirement 1 : match at least what Excel does for dates;
 * Requirement 2 : class is immutable;
 * <P>
 * Why not just use java.util.Date?  We will, when it makes sense.  At times,
 * java.util.Date can be *too* precise - it represents an instant in time,
 * accurate to 1/1000th of a second (with the date itself depending on the
 * time-zone).  Sometimes we just want to represent a particular day (e.g. 21
 * January 2015) without concerning ourselves about the time of day, or the
 * time-zone, or anything else.  That's what we've defined SerialDate for.
 * <P>
 * You can call getInstance() to get a concrete subclass of SerialDate,
 * without worrying about the exact implementation.
 *
 * @author David Gilbert
 */
public abstract class SerialDateImpl implements Comparable<SerialDate>, Serializable, SerialDate {

    public static SerialDateImpl addDays(int days, SerialDate base) {
        return SerialDateImpl.createInstance(base.toSerial() + days);
    }

    /**
     * Creates a new date by adding the specified number of months to the base
     * date.
     * <P>
     * If the base date is close to the end of the month, the day on the result
     * may be adjusted slightly:  31 May + 1 month = 30 June.
     *
     * @param months the number of months to add (can be negative).
     * @param base the base date.
     * @return a new date.
     */
    public static SerialDate addMonths(int months, SerialDate base) {
        int calculatedMonths = 12 * base.getYear() + base.getMonth() + months - 1;

        int newYear = calculatedMonths / 12;
        int newMonth = calculatedMonths % 12 + 1;
        int newDayOfMonth =
            Math.min(base.getDayOfMonth(), SerialDateUtilities.lastDayOfMonth(newMonth, newYear));
        return SerialDateImpl.createInstance(newDayOfMonth, newMonth, newYear);

    }

    /**
     * Creates a new date by adding the specified number of years to the base
     * date.
     *
     * @param years the number of years to add (can be negative).
     * @param base the base date.
     * @return A new date.
     */
    public static SerialDate addYears(int years, final SerialDate base) {
        int year = base.getYear();
        int month = base.getMonth();
        int dayOfMonth = base.getDayOfMonth();

        int newYear = year + years;
        int newDayOfMonth = Math
            .min(dayOfMonth, SerialDateUtilities.lastDayOfMonth(month, newYear));

        return SerialDateImpl.createInstance(newDayOfMonth, month, newYear);
    }

    /**
     * Returns the latest date that falls on the specified day-of-the-week and
     * is BEFORE the base date.
     *
     * @param targetWeekday a code for the target day-of-the-week.
     * @param base the base date.
     * @return the latest date that falls on the specified day-of-the-week and is BEFORE the base
     * date.
     */
    public static SerialDate getPreviousDayOfWeek(int targetWeekday, SerialDate base) {
        SerialDateUtilities.checkValidDayOfWeek(targetWeekday);

        int adjust = SerialDateUtilities.calculateDateAdjustment(targetWeekday, base);
        return SerialDateImpl.addDays(adjust, base);
    }

    /**
     * Returns the earliest date that falls on the specified day-of-the-week
     * and is AFTER the base date.
     *
     * @param targetWeekday a code for the target day-of-the-week.
     * @param base the base date.
     * @return the earliest date that falls on the specified day-of-the-week and is AFTER the base
     * date.
     */
    public static SerialDate getFollowingDayOfWeek(int targetWeekday, SerialDate base) {
        SerialDateUtilities.checkValidDayOfWeek(targetWeekday);

        // TODO use calculateDateAdjustment
        // find the date...
        final int adjust;
        final int baseDOW = base.getDayOfWeek();
        if (baseDOW > targetWeekday) {
            adjust = 7 + Math.min(0, targetWeekday - baseDOW);
        } else {
            adjust = Math.max(0, targetWeekday - baseDOW);
        }

        return SerialDateImpl.addDays(adjust, base);
    }

    /**
     * Returns the date that falls on the specified day-of-the-week and is
     * CLOSEST to the base date.
     *
     * @param targetDOW a code for the target day-of-the-week.
     * @param base the base date.
     * @return the date that falls on the specified day-of-the-week and is CLOSEST to the base date.
     */
    public static SerialDate getNearestDayOfWeek(final int targetDOW,
        final SerialDate base) {
        SerialDateUtilities.checkValidDayOfWeek(targetDOW);

        // find the date...
        int baseDOW = base.getDayOfWeek();
        int adjust = -Math.abs(targetDOW - baseDOW);
        if (adjust >= 4) {
            adjust = 7 - adjust;
        }
        if (adjust <= -4) {
            adjust = 7 + adjust;
        }
        return SerialDateImpl.addDays(adjust, base);

    }

    /**
     * Rolls the date forward to the last day of the month.
     *
     * @param base the base date.
     * @return a new serial date.
     */
    public SerialDateImpl getEndOfCurrentMonth(final SerialDate base) {
        final int last = SerialDateUtilities.lastDayOfMonth(
            base.getMonth(), base.getYear()
        );
        return SerialDateImpl.createInstance(last, base.getMonth(), base.getYear());
    }

    /**
     * Factory method that returns an instance of some concrete subclass of
     * {@link SerialDateImpl}.
     *
     * @param day the day (1-31).
     * @param month the month (1-12).
     * @param year the year (in the range 1900 to 9999).
     * @return An instance of {@link SerialDateImpl}.
     */
    public static SerialDateImpl createInstance(int day, int month, int year) {
        return new SpreadsheetDate(day, month, year);
    }

    public static SerialDateImpl createInstance(int day, Month month, int year) {
        return new SpreadsheetDate(day, month, year);
    }

    /**
     * Factory method that returns an instance of some concrete subclass of
     * {@link SerialDateImpl}.
     *
     * @param serial the serial number for the day (1 January 1900 = 2).
     * @return a instance of SerialDate.
     */
    public static SerialDateImpl createInstance(final int serial) {
        return new SpreadsheetDate(serial);
    }

    /**
     * Factory method that returns an instance of a subclass of SerialDate.
     *
     * @param date A Java date object.
     * @return a instance of SerialDate.
     */
    public static SerialDate createInstance(java.util.Date date) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return new SpreadsheetDate(calendar.get(Calendar.DATE),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.YEAR));

    }

    /**
     * Returns the latest date that falls on the specified day-of-the-week and
     * is BEFORE this date.
     *
     * @param targetDOW a code for the target day-of-the-week.
     * @return the latest date that falls on the specified day-of-the-week and is BEFORE this date.
     */
    public SerialDate getPreviousDayOfWeek(final int targetDOW) {
        return getPreviousDayOfWeek(targetDOW, this);
    }

    /**
     * Returns the earliest date that falls on the specified day-of-the-week
     * and is AFTER this date.
     *
     * @param targetDOW a code for the target day-of-the-week.
     * @return the earliest date that falls on the specified day-of-the-week and is AFTER this date.
     */
    public SerialDate getFollowingDayOfWeek(final int targetDOW) {
        return getFollowingDayOfWeek(targetDOW, this);
    }

    /**
     * Returns the nearest date that falls on the specified day-of-the-week.
     *
     * @param targetDOW a code for the target day-of-the-week.
     * @return the nearest date that falls on the specified day-of-the-week.
     */
    public SerialDate getNearestDayOfWeek(final int targetDOW) {
        return getNearestDayOfWeek(targetDOW, this);
    }

    @Override
    public String toString() {
        return getDayOfMonth() + "-" + Month.monthCodeToLongName(getMonth()) + "-" + getYear();
    }
}
