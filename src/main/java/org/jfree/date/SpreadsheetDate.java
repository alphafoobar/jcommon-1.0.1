/*
 * (C) Copyright 2000-2005, by Object Refinery Limited .
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
 */
package org.jfree.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.Nonnull;

/**
 * Represents a date in the range of dates supported is
 * 1-Jan-1900 to 31-Dec-9999.
 *
 * This class uses the convention 1-Jan-1900 = 2.
 *
 * The result is that the day number in this class will be different to the
 * Excel figure for January and February 1900...but then Excel adds in an extra
 * day (29-Feb-1900 which does not actually exist!) and from that point forward
 * the day numbers will match.
 *
 * @author David Gilbert
 */
public class SpreadsheetDate implements Serializable, SerialDate {
    private static final long serialVersionUID = 0L;

    private int serial;
    private int day;
    private int month;
    private int year;

    private SpreadsheetDate(int day, Month month, int year) {
        SerialDateUtilities.validate(day, month.getMonthCode(), year);

        this.year = year;
        this.month = month.getMonthCode();
        this.day = day;

        // the serial number needs to be synchronised with the day-month-year...
        this.serial = calculateSerial(day, month.getMonthCode(), year);
    }

    private SpreadsheetDate(int serial) {
        SerialDateUtilities.checkValidSerial(serial);

        this.serial = serial;

        // the day-month-year needs to be synchronised with the serial number...
        calculateAndSetDayMonthYearFromSerial();
    }

    public static SerialDate createInstance(int day, int month, int year) {
        return new SpreadsheetDate(day, Month.fromMonthCode(month), year);
    }

    public static SerialDate createInstance(int day, Month month, int year) {
        return new SpreadsheetDate(day, month, year);
    }

    public static SerialDate createInstance(int serial) {
        return new SpreadsheetDate(serial);
    }

    public static SerialDate createInstance(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.clear();
        calendar.setTime(date);
        return new SpreadsheetDate(calendar.get(Calendar.DATE),
            Month.fromCalendarMonthCode(calendar.get(Calendar.MONTH)),
            calendar.get(Calendar.YEAR));
    }

    @Override
    public SerialDate addDays(int days) {
        return createInstance(toSerial() + days);
    }

    @Override
    public SerialDate getEndOfCurrentMonth() {
        int last = SerialDateUtilities.lastDayOfMonth(month, year);
        return createInstance(last, Month.fromMonthCode(month), year);
    }

    @Override
    public SerialDate addYears(int years) {
        int newYear = year + years;
        int newDayOfMonth = Math
            .min(day, SerialDateUtilities.lastDayOfMonth(month, newYear));
        return createInstance(newDayOfMonth, Month.fromMonthCode(month), newYear);
    }

    @Override
    public SerialDate addMonths(int months) {
        int calculatedMonths = 12 * year + month + months - 1;

        int newYear = calculatedMonths / 12;
        int newMonth = calculatedMonths % 12 + 1;
        int newDayOfMonth =
            Math.min(day, SerialDateUtilities.lastDayOfMonth(newMonth, newYear));
        return createInstance(newDayOfMonth, Month.fromMonthCode(newMonth), newYear);
    }

    @Override
    public SerialDate getFollowingDayOfWeek(int targetWeekday) {
        SerialDateUtilities.checkValidDayOfWeek(targetWeekday);

        int difference = -SerialDateUtilities
            .calculateDifference(targetWeekday, calculateDayOfWeek());
        return addDays(difference <= 0 ? difference + 7 : difference);
    }

    @Override
    public SerialDate getNearestDayOfWeek(int targetWeekday) {
        SerialDateUtilities.checkValidDayOfWeek(targetWeekday);

        // find the date...
        int adjust = -Math.abs(targetWeekday - calculateDayOfWeek());
        if (adjust <= -4) {
            adjust = 7 + adjust;
        }
        return addDays(adjust);
    }

    @Override
    public SerialDate getPreviousDayOfWeek(int targetWeekday) {
        SerialDateUtilities.checkValidDayOfWeek(targetWeekday);

        int difference = SerialDateUtilities
            .calculateDifference(calculateDayOfWeek(), targetWeekday);
        return addDays(difference >= 0 ? difference - 7 : difference);
    }

    @Override
    public int toSerial() {
        return this.serial;
    }

    @Override
    public Date toDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(getYear(), getMonth() - 1, getDayOfMonth(), 0, 0, 0);
        return calendar.getTime();
    }

    @Override
    public int getYear() {
        return this.year;
    }

    @Override
    public int getMonth() {
        return this.month;
    }

    @Override
    public int getDayOfMonth() {
        return this.day;
    }

    @Override
    public int calculateDayOfWeek() {
        return (this.serial + 6) % 7 + 1;
    }

    @Override
    public int compareTo(@Nonnull SerialDate other) {
        return this.serial - other.toSerial();
    }

    @Override
    public boolean isOn(@Nonnull SerialDate other) {
        return compareTo(other) == 0;
    }

    @Override
    public boolean isBefore(@Nonnull SerialDate other) {
        return compareTo(other) < 0;
    }

    @Override
    public boolean isOnOrBefore(@Nonnull SerialDate other) {
        return compareTo(other) <= 0;
    }

    @Override
    public boolean isAfter(@Nonnull SerialDate other) {
        return compareTo(other) > 0;
    }

    @Override
    public boolean isOnOrAfter(@Nonnull SerialDate other) {
        return compareTo(other) >= 0;
    }

    @Override
    public boolean isInRange(@Nonnull SerialDate dateFrom, @Nonnull SerialDate dateTo) {
        return isInRange(dateFrom, dateTo, INCLUDE_BOTH);
    }

    @Override
    public boolean isInRange(@Nonnull SerialDate dateFrom, @Nonnull SerialDate dateTo,
        int inclusionRule) {
        int start = dateFrom.toSerial();
        int end = dateTo.toSerial();

        if (inclusionRule == INCLUDE_BOTH) {
            return serial >= start && serial <= end;
        }

        if (inclusionRule == INCLUDE_FIRST) {
            return serial >= start && serial < end;
        }

        if (inclusionRule == INCLUDE_SECOND) {
            return serial > start && serial <= end;
        }

        return serial > start && serial < end;
    }

    private static int calculateSerial(int day, int month, int year) {
        int daysTillYear = calculateDaysIgnoringLeapYears(year);
        int aggregatedDays = calculateAggregatedDays(month, year);
        return daysTillYear + aggregatedDays + day + 1;
    }

    private static int calculateAggregatedDays(int month, int year) {
        int aggregatedDays = AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH[month];
        if (month > Month.FEBRUARY.getMonthCode() && SerialDateUtilities.isLeapYear(year)) {
            aggregatedDays = aggregatedDays + 1;
        }
        return aggregatedDays;
    }

    private static int calculateDaysIgnoringLeapYears(int year) {
        int daysSinceMinimumYear = (year - MINIMUM_YEAR_SUPPORTED) * 365;
        return daysSinceMinimumYear + SerialDateUtilities
            .leapYearCount(year > MINIMUM_YEAR_SUPPORTED ? year - 1 : MINIMUM_YEAR_SUPPORTED);
    }

    private static int calculateYearIgnoringLeapYears(int days) {
        return MINIMUM_YEAR_SUPPORTED + (days / 365);
    }

    private void calculateAndSetDayMonthYearFromSerial() {
        // get the year from the serial date
        int days = this.serial - SERIAL_LOWER_BOUND;

        setYearFromSerialDays(days);

        int ss2 = calculateSerial(1, 1, this.year);

        int[] daysToEndOfPrecedingMonth = AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH;
        if (SerialDateUtilities.isLeapYear(this.year)) {
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
            days - SerialDateUtilities.leapYearCount(yearIgnoringLeapYears));

        if (yearWithOverEstimatedLeapYears == yearIgnoringLeapYears) {
            this.year = yearWithOverEstimatedLeapYears;
        } else {
            int ss1 = calculateSerial(1, 1, yearWithOverEstimatedLeapYears);
            while (ss1 <= this.serial) {
                yearWithOverEstimatedLeapYears = yearWithOverEstimatedLeapYears + 1;
                ss1 = calculateSerial(1, 1, yearWithOverEstimatedLeapYears);
            }

            this.year = yearWithOverEstimatedLeapYears - 1;
        }
    }

    @Override
    public int hashCode() {
        return serial;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SpreadsheetDate) {
            SpreadsheetDate s = (SpreadsheetDate) object;
            return s.serial == serial;
        }
        return false;
    }

    @Override
    public String toString() {
        return getDayOfMonth() + "-" + Month.monthCodeToLongName(getMonth()) + "-" + getYear();
    }

}
