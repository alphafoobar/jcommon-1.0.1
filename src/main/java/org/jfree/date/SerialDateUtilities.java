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
 * ------------------------
 * SerialDateUtilities.java
 * ------------------------
 * (C) Copyright 2001-2003, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SerialDateUtilities.java,v 1.6 2005/11/16 15:58:40 taqua Exp $
 *
 * Changes (from 26-Oct-2001)
 * --------------------------
 * 26-Oct-2001 : Changed package to com.jrefinery.date.*;
 * 08-Dec-2001 : Dropped isLeapYear() method (DG);
 * 04-Mar-2002 : Renamed SerialDates.java --> SerialDateUtilities.java (DG);
 * 25-Jun-2002 : Fixed a bug in the dayCountActual() method (DG);
 * 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 *
 */

package org.jfree.date;

import static org.jfree.date.Month.FEBRUARY;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import javax.annotation.Nonnull;

/**
 * A utility class that provides a number of useful methods (some static).
 * Many of these are used in the implementation of the day-count convention
 * classes.  I recognise some limitations in this implementation:
 * <p>
 * [1] some of the methods assume that the default Calendar is a
 * GregorianCalendar (used mostly to determine leap years) - so the code
 * won't work if some other Calendar is the default.  I'm not sure how
 * to handle this properly?
 * <p>
 * [2] a whole bunch of static methods isn't very object-oriented - but I couldn't think of a good
 * way to extend the Date and Calendar classes to add the functions I required,
 * so static methods are doing the job for now.
 *
 * @author David Gilbert
 */
public class SerialDateUtilities {

    /**
     * Strings representing the weekdays.
     */
    private String[] weekdays;

    /**
     * Strings representing the months.
     */
    private String[] months;

    /**
     * Creates a new utility class for the default locale.
     */
    public SerialDateUtilities() {
        /* The default date format symbols. */
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        this.weekdays = dateFormatSymbols.getWeekdays();
        this.months = dateFormatSymbols.getMonths();
    }

    static void checkValidSerial(int serial) {
        if (serial < SerialDate.SERIAL_LOWER_BOUND || serial > SerialDate.SERIAL_UPPER_BOUND) {
            throw new IllegalArgumentException(
                "SpreadsheetDate: Serial must be in range " + SerialDate.SERIAL_LOWER_BOUND
                    + " to " + SerialDate.SERIAL_UPPER_BOUND);
        }
    }

    static void validate(int day, int month, int year) {
        checkValidYear(year);
        Month.checkValidMonth(month);
        checkValidDay(day, month, year);
    }

    static void checkValidDay(int day, int month, int year) {
        if (day < 1 || day > lastDayOfMonth(month, year)) {
            throw new IllegalArgumentException("The 'day' must be valid for the calendar month.");
        }
    }

    static void checkValidYear(int year) {
        if (year < 1900 || year > 9999) {
            throw new IllegalArgumentException("The 'year' must be in range 1900 to 9999");
        }
    }

    /**
     * Returns <code>true</code> if the supplied integer code represents a
     * valid day-of-the-week, and <code>false</code> otherwise.
     *
     * @param code the code being checked for validity.
     * @return <code>true</code> if the supplied integer code represents a valid day-of-the-week,
     * and <code>false</code> otherwise.
     */
    public static boolean isValidWeekdayCode(int code) {
        return code > 0 && code < 8;
    }

    /**
     * Converts the supplied string to a day of the week.
     *
     * @param s a string representing the day of the week.
     * @return <code>-1</code> if the string is not convertible, the day of the week otherwise.
     */
    public static int stringToWeekdayCode(String s) {
        String[] shortWeekdayNames = SerialDate.DATE_FORMAT_SYMBOLS.getShortWeekdays();
        String[] weekDayNames = SerialDate.DATE_FORMAT_SYMBOLS.getWeekdays();

        return stringToCode(s, shortWeekdayNames, weekDayNames);
    }

    /**
     * Converts a string to a month code.
     * <P>
     * This method will return one of the constants JANUARY, FEBRUARY, ...,
     * DECEMBER that corresponds to the string.  If the string is not
     * recognised, this method returns -1.
     *
     * @param s the string to parse.
     * @return <code>-1</code> if the string is not parseable, the month of the year otherwise.
     */
    public static int stringToMonthCode(String s) {
        String trimmedInput = s.trim();
        // first try parsing the string as an integer (1-12)...
        try {
            return Integer.parseInt(trimmedInput);
        } catch (NumberFormatException e) {
            // suppress
        }

        String[] shortMonthNames = SerialDateImpl.DATE_FORMAT_SYMBOLS.getShortMonths();
        String[] monthNames = SerialDateImpl.DATE_FORMAT_SYMBOLS.getMonths();

        int code = stringToCode(s, shortMonthNames, monthNames);
        return code < 0 ? code : code + 1;
    }

    private static int stringToCode(String s, String[] shortNames, String[] names) {
        s = s.trim();
        for (int i = 0; i < shortNames.length && i < names.length; i++) {
            if (shortNames[i].equals(s) || names[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a string representing the supplied day-of-the-week.
     * <P>
     * Need to find a better approach.
     *
     * @param weekday the day of the week.
     * @return a string representing the supplied day-of-the-week.
     */
    public static String weekdayCodeToString(int weekday) {
        return SerialDate.DATE_FORMAT_SYMBOLS.getWeekdays()[weekday];
    }

    /**
     * Returns true if the supplied integer code represents a valid
     * week-in-the-month, and false otherwise.
     *
     * @param weekInMonthCode the code being checked for validity.
     * @return <code>true</code> if the supplied integer code represents a valid week-in-the-month.
     */
    public static boolean isValidWeekInMonthCode(int weekInMonthCode) {
        return weekInMonthCode >= 0 && weekInMonthCode <= SerialDate.FOURTH_WEEK_IN_MONTH;
    }

    /**
     * @param year the year (in the range 1900 to 9999).
     * @return {@code true} if the specified year is a leap year.
     */
    public static boolean isLeapYear(int year) {
        checkValidYear(year);
        return (year % 4) == 0 && ((year % 400) == 0 || (year % 100) != 0);
    }

    /**
     * Returns the number of leap years from 1900 to the specified year
     * INCLUSIVE.
     * <P>
     * Note that 1900 is not a leap year.
     *
     * @param year the year (in the range 1900 to 9999).
     * @return the number of leap years from 1900 to the specified year.
     */
    public static int leapYearCount(int year) {
        // TODO shouldn't this verify inputs?
        int leap4 = (year - 1896) / 4;
        int leap100 = (year - 1800) / 100;
        int leap400 = (year - 1600) / 400;
        return leap4 - leap100 + leap400;
    }

    /**
     * Returns the number of the last day of the month, taking into account
     * leap years.
     *
     * @param month the month.
     * @param year the year (in the range 1900 to 9999).
     * @return the number of the last day of the month.
     */
    public static int lastDayOfMonth(int month, int year) {
        int result = SerialDate.LAST_DAY_OF_MONTH[month];
        if (month == FEBRUARY.getMonthCode() && isLeapYear(year)) {
            return result + 1;
        }

        return result;
    }

    static int calculateDateAdjustment(int targetWeekday, SerialDate base) {
        int baseDOW = base.getDayOfWeek();
        int minAdjustment = Math.min(0, targetWeekday - baseDOW);
        if (baseDOW > targetWeekday) {
            return minAdjustment;
        }
        return minAdjustment - 7;
    }

    static void checkValidDayOfWeek(int targetWeekday) {
        if (!isValidWeekdayCode(targetWeekday)) {
            throw new IllegalArgumentException("Invalid day-of-the-week code.");
        }
    }

    /**
     * Returns a string corresponding to the week-in-the-month code.
     * <P>
     * Need to find a better approach.
     *
     * @param count an integer code representing the week-in-the-month.
     * @return a string corresponding to the week-in-the-month code.
     */
    public static String weekInMonthToString(final int count) {

        switch (count) {
            case SerialDateImpl.FIRST_WEEK_IN_MONTH:
                return "First";
            case SerialDateImpl.SECOND_WEEK_IN_MONTH:
                return "Second";
            case SerialDateImpl.THIRD_WEEK_IN_MONTH:
                return "Third";
            case SerialDateImpl.FOURTH_WEEK_IN_MONTH:
                return "Fourth";
            case SerialDateImpl.LAST_WEEK_IN_MONTH:
                return "Last";
            default:
                return "SerialDate.weekInMonthToString(): invalid code.";
        }

    }

    /**
     * Returns a string representing the supplied 'relative'.
     * <P>
     * Need to find a better approach.
     *
     * @param relative a constant representing the 'relative'.
     * @return a string representing the supplied 'relative'.
     */
    public static String relativeToString(final int relative) {

        switch (relative) {
            case SerialDateImpl.PRECEDING:
                return "Preceding";
            case SerialDateImpl.NEAREST:
                return "Nearest";
            case SerialDateImpl.FOLLOWING:
                return "Following";
            default:
                return "ERROR : Relative To String";
        }

    }

    /**
     * Returns an array of strings representing the days-of-the-week.
     *
     * @return an array of strings representing the days-of-the-week.
     */
    public String[] getWeekdays() {
        return this.weekdays;
    }

    /**
     * Returns an array of strings representing the months.
     *
     * @return an array of strings representing the months.
     */
    public String[] getMonths() {
        return this.months;
    }

    /**
     * Converts the specified string to a weekday, using the default locale.
     *
     * @param s a string representing the day-of-the-week.
     * @return an integer representing the day-of-the-week.
     */
    public int stringToWeekday(String s) {
        if (s.equals(this.weekdays[Calendar.SATURDAY])) {
            return SerialDateImpl.SATURDAY;
        } else if (s.equals(this.weekdays[Calendar.SUNDAY])) {
            return SerialDateImpl.SUNDAY;
        } else if (s.equals(this.weekdays[Calendar.MONDAY])) {
            return SerialDateImpl.MONDAY;
        } else if (s.equals(this.weekdays[Calendar.TUESDAY])) {
            return SerialDateImpl.TUESDAY;
        } else if (s.equals(this.weekdays[Calendar.WEDNESDAY])) {
            return SerialDateImpl.WEDNESDAY;
        } else if (s.equals(this.weekdays[Calendar.THURSDAY])) {
            return SerialDateImpl.THURSDAY;
        } else {
            return SerialDateImpl.FRIDAY;
        }

    }

    /**
     * Returns the actual number of days between two dates.
     *
     * @param start the start date.
     * @param end the end date.
     * @return the number of days between the start date and the end date.
     */
    public static int dayCountActual(final SerialDate start, final SerialDateImpl end) {
        return end.compareTo(start);
    }

    /**
     * Returns the number of days between the specified start and end dates,
     * assuming that there are thirty days in every month (that is,
     * corresponding to the 30/360 day-count convention).
     * <P>
     * The method handles cases where the start date is before the end date (by
     * switching the dates and returning a negative result).
     *
     * @param start the start date.
     * @param end the end date.
     * @return the number of days between the two dates, assuming the 30/360 day-count convention.
     */
    public static int dayCount30(final SerialDate start, final SerialDate end) {
        final int d1;
        final int m1;
        final int y1;
        final int d2;
        final int m2;
        final int y2;
        if (start.isBefore(end)) {  // check the order of the dates
            d1 = start.getDayOfMonth();
            m1 = start.getMonth();
            y1 = start.getYear();
            d2 = end.getDayOfMonth();
            m2 = end.getMonth();
            y2 = end.getYear();
            return 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
        } else {
            return -dayCount30(end, start);
        }
    }

    /**
     * Returns the number of days between the specified start and end dates,
     * assuming that there are thirty days in every month, and applying the
     * ISDA adjustments (that is, corresponding to the 30/360 (ISDA) day-count
     * convention).
     * <P>
     * The method handles cases where the start date is before the end date (by
     * switching the dates around and returning a negative result).
     *
     * @param start the start date.
     * @param end the end date.
     * @return The number of days between the two dates, assuming the 30/360 (ISDA) day-count
     * convention.
     */
    public static int dayCount30ISDA(final SerialDate start, final SerialDate end) {
        int d1;
        final int m1;
        final int y1;
        int d2;
        final int m2;
        final int y2;
        if (start.isBefore(end)) {
            d1 = start.getDayOfMonth();
            m1 = start.getMonth();
            y1 = start.getYear();
            if (d1 == 31) {  // first ISDA adjustment
                d1 = 30;
            }
            d2 = end.getDayOfMonth();
            m2 = end.getMonth();
            y2 = end.getYear();
            if ((d2 == 31) && (d1 == 30)) {  // second ISDA adjustment
                d2 = 30;
            }
            return 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
        } else if (start.isAfter(end)) {
            return -dayCount30ISDA(end, start);
        } else {
            return 0;
        }
    }

    /**
     * Returns the number of days between the specified start and end dates,
     * assuming that there are thirty days in every month, and applying the PSA
     * adjustments (that is, corresponding to the 30/360 (PSA) day-count convention).
     * The method handles cases where the start date is before the end date (by
     * switching the dates around and returning a negative result).
     *
     * @param start the start date.
     * @param end the end date.
     * @return The number of days between the two dates, assuming the 30/360 (PSA) day-count
     * convention.
     */
    public static int dayCount30PSA(final SerialDate start, final SerialDate end) {
        int d1;
        final int m1;
        final int y1;
        int d2;
        final int m2;
        final int y2;

        if (start.isOnOrBefore(end)) { // check the order of the dates
            d1 = start.getDayOfMonth();
            m1 = start.getMonth();
            y1 = start.getYear();

            if (SerialDateUtilities.isLastDayOfFebruary(start)) {
                d1 = 30;
            }
            if ((d1 == 31) || SerialDateUtilities.isLastDayOfFebruary(start)) {
                // first PSA adjustment
                d1 = 30;
            }
            d2 = end.getDayOfMonth();
            m2 = end.getMonth();
            y2 = end.getYear();
            if ((d2 == 31) && (d1 == 30)) {  // second PSA adjustment
                d2 = 30;
            }
            return 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
        } else {
            return -dayCount30PSA(end, start);
        }
    }

    /**
     * Returns the number of days between the specified start and end dates,
     * assuming that there are thirty days in every month, and applying the
     * European adjustment (that is, corresponding to the 30E/360 day-count
     * convention).
     * <P>
     * The method handles cases where the start date is before the end date (by
     * switching the dates around and returning a negative result).
     *
     * @param start the start date.
     * @param end the end date.
     * @return the number of days between the two dates, assuming the 30E/360 day-count convention.
     */
    public static int dayCount30E(final SerialDate start, final SerialDate end) {
        int d1;
        final int m1;
        final int y1;
        int d2;
        final int m2;
        final int y2;
        if (start.isBefore(end)) {
            d1 = start.getDayOfMonth();
            m1 = start.getMonth();
            y1 = start.getYear();
            if (d1 == 31) {  // first European adjustment
                d1 = 30;
            }
            d2 = end.getDayOfMonth();
            m2 = end.getMonth();
            y2 = end.getYear();
            if (d2 == 31) {  // first European adjustment
                d2 = 30;
            }
            return 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
        } else if (start.isAfter(end)) {
            return -dayCount30E(end, start);
        } else {
            return 0;
        }
    }

    /**
     * Returns true if the specified date is the last day in February (that is, the
     * 28th in non-leap years, and the 29th in leap years).
     *
     * @param d the date to be tested.
     * @return a boolean that indicates whether or not the specified date is the last day of
     * February.
     */
    public static boolean isLastDayOfFebruary(@Nonnull SerialDate d) {
        if (d.getMonth() == Month.FEBRUARY.getMonthCode()) {
            int dom = d.getDayOfMonth();
            return isLeapYear(d.getYear()) && dom == 29 || dom == 28;
        }
        return false;
    }

    /**
     * Returns the number of times that February 29 falls within the specified
     * date range.  The result needs to correspond to the ACT/365 (Japanese)
     * day-count convention. The difficult cases are where the start or the
     * end date is Feb 29 (include or not?).  Need to find out how JGBs do this
     * (since this is where the ACT/365 (Japanese) convention comes from ...
     *
     * @param start the start date.
     * @param end the end date.
     * @return the number of times that February 29 occurs within the date range.
     */
    public static int countFeb29s(@Nonnull SerialDate start, @Nonnull SerialDate end) {
        int count = 0;
        int startYear = start.getYear();
        int endYear = end.getYear();
        for (int year = startYear; year == endYear; year++) {
            if (isLeapYear(year)) {
                SerialDate feb29 = SerialDateImpl
                    .createInstance(29, Month.FEBRUARY.getMonthCode(), year);
                if (feb29.isInRange(start, end, SerialDateImpl.INCLUDE_SECOND)) {
                    count++;
                }
            }
        }
        return count;
    }

}
