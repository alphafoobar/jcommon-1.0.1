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
 * ------------------
 * DateUtilities.java
 * ------------------
 * (C) Copyright 2002, 2003, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: DateUtilities.java,v 1.4 2005/11/16 15:58:40 taqua Exp $
 *
 * Changes
 * -------
 * 11-Oct-2002 : Version 1 (DG);
 * 03-Apr-2003 : Added clear() method call (DG)
 *
 */

package org.jfree.date;

import java.util.Calendar;
import java.util.Date;

public class DateUtilities {

    /**
     * @param year the year.
     * @param month the month (1 - 12).
     * @param day the day.
     * @return a date.
     */
    public static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * @param year the year.
     * @param month the month (1 - 12).
     * @param day the day.
     * @param hour the hour.
     * @param min the minute.
     * @return a date.
     */
    public static Date createDate(int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month - 1, day, hour, min);
        return calendar.getTime();
    }

    public static Date createDateFromSerialDate(SerialDate serialDate) {
        return serialDate.toDate();
    }
}
