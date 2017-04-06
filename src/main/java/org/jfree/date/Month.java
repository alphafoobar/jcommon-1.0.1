/*
 * (C) Copyright 2000-2005, by Object Refinery Limited.
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
 */
package org.jfree.date;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public enum Month {
    JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(8),
    SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12);

    private final int monthCode;
    private final int calendarMonthCode;
    private final String longName;
    private final String shortName;

    Month(int monthCode) {
        this.monthCode = monthCode;
        calendarMonthCode = monthCode - 1;
        DateFormatSymbols symbols = new SimpleDateFormat().getDateFormatSymbols();
        shortName = symbols.getShortMonths()[calendarMonthCode];
        longName = symbols.getMonths()[calendarMonthCode];
    }

    public int getMonthCode() {
        return monthCode;
    }

    int getCalendarMonthCode() {
        return calendarMonthCode;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    /**
     * Returns the quarter for the specified month.
     *
     * @param code the month code (1-12).
     * @return the quarter that the month belongs to.
     */
    public static int monthCodeToQuarter(int code) {
        checkValidMonth(code);
        if (code % 3 == 0) {
            return code / 3;
        }
        return code / 3 + 1;
    }

    public static String monthCodeToLongName(int month) {
        return fromMonthCode(month).getLongName();
    }

    public static String monthCodeToShortName(int month) {
        return fromMonthCode(month).getShortName();
    }

    public static Month fromMonthCode(int monthCode) {
        checkValidMonth(monthCode);

        for (Month month : Month.values()) {
            if (month.monthCode == monthCode) {
                return month;
            }
        }
        // Unreachable exception
        throw new IllegalArgumentException("Not found: " + monthCode);
    }

    static Month fromCalendarMonthCode(int calendarMonthCode) {
        return fromMonthCode(calendarMonthCode + 1);
    }

    /**
     * @return {@code true} if a valid month.
     */
    public static boolean isValidMonth(int month) {
        return month >= Month.JANUARY.getMonthCode() && month <= Month.DECEMBER.getMonthCode();
    }

    public static void checkValidMonth(int month) {
        if (!Month.isValidMonth(month)) {
            throw new IllegalArgumentException("The 'month' must be in the range 1 to 12");
        }
    }
}
