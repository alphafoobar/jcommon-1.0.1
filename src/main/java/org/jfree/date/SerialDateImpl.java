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
 */

package org.jfree.date;

import java.io.Serializable;

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
public abstract class SerialDateImpl implements Serializable, SerialDate {

    /**
     * Returns the date that falls on the specified day-of-the-week and is
     * CLOSEST to the base date.
     *
     * @param targetDOW a code for the target day-of-the-week.
     * @param base the base date.
     * @return the date that falls on the specified day-of-the-week and is CLOSEST to the base date.
     */
    public static SerialDate getNearestDayOfWeek(int targetDOW, SerialDate base) {
        SerialDateUtilities.checkValidDayOfWeek(targetDOW);

        // find the date...
        int baseDOW = base.calculateDayOfWeek();
        int adjust = -Math.abs(targetDOW - baseDOW);
        if (adjust <= -4) {
            adjust = 7 + adjust;
        }
        return base.addDays(adjust);

    }

    /**
     * Returns the latest date that falls on the specified day-of-the-week and
     * is BEFORE this date.
     *
     * @param targetWeekday a code for the target day-of-the-week.
     * @return the latest date that falls on the specified day-of-the-week and is BEFORE this date.
     */
    @Override
    public SerialDate getPreviousDayOfWeek(int targetWeekday) {
        SerialDateUtilities.checkValidDayOfWeek(targetWeekday);

        return addDays(SerialDateUtilities.calculateDateAdjustment(targetWeekday, this));
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
