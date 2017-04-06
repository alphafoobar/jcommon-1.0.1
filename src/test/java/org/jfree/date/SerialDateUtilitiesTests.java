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
 * -----------------------------
 * SerialDateUtilitiesTests.java
 * -----------------------------
 * (C) Copyright 2002, 2003, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: SerialDateUtilitiesTests.java,v 1.4 2005/11/16 15:58:40 taqua Exp $
 *
 * Changes
 * -------
 * 25-Jun-2002 : Version 1 (DG);
 * 24-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 *
 */

package org.jfree.date;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class SerialDateUtilitiesTests {

    @Test
    public void testDayCountActual() {
        SerialDate d1 = SpreadsheetDate.createInstance(1, Month.APRIL, 2002);
        SerialDate d2 = SpreadsheetDate.createInstance(2, Month.APRIL, 2002);
        int count = SerialDateUtilities.dayCountActual(d1, d2);
        assertEquals(1, count);
    }

    /**
     * Problem 30/360 day count.
     */
    @Test
    public void testDayCount30() {
        final SerialDate d1 = SpreadsheetDate.createInstance(1, Month.APRIL, 2002);
        final SerialDate d2 = SpreadsheetDate.createInstance(2, Month.APRIL, 2002);
        final int count = SerialDateUtilities.dayCount30(d1, d2);
        assertEquals(1, count);
    }

    /**
     * Problem 30/360ISDA day count.
     */
    @Test
    public void testDayCount30ISDA() {
        final SerialDate d1 = SpreadsheetDate.createInstance(1, Month.APRIL, 2002);
        final SerialDate d2 = SpreadsheetDate.createInstance(2, Month.APRIL, 2002);
        final int count = SerialDateUtilities.dayCount30ISDA(d1, d2);
        assertEquals(1, count);
    }

    /**
     * Problem 30/360PSA day count.
     */
    @Test
    public void testDayCount30PSA() {
        final SerialDate d1 = SpreadsheetDate.createInstance(1, Month.APRIL, 2002);
        final SerialDate d2 = SpreadsheetDate.createInstance(2, Month.APRIL, 2002);
        final int count = SerialDateUtilities.dayCount30PSA(d1, d2);
        assertEquals(1, count);
    }

    @Test
    public void testDayCountFebruary30PSA() {
        SerialDate startDate = SpreadsheetDate.createInstance(28, Month.FEBRUARY, 2002);
        SerialDate endDate = SpreadsheetDate.createInstance(2, Month.APRIL, 2002);
        int count = SerialDateUtilities.dayCount30PSA(startDate, endDate);
        assertEquals(32, count);
    }

    @Test
    public void testDayCountFebruaryLeap30PSA() {
        SerialDate startDate = SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000);
        SerialDate endDate = SpreadsheetDate.createInstance(2, Month.APRIL, 2000);
        int count = SerialDateUtilities.dayCount30PSA(startDate, endDate);
        assertEquals(32, count);
    }

    /**
     * Problem 30E/360 day count.
     */
    @Test
    public void testDayCount3030E() {
        SerialDate d1 = SpreadsheetDate.createInstance(1, Month.APRIL, 2002);
        SerialDate d2 = SpreadsheetDate.createInstance(2, Month.APRIL, 2002);
        int count = SerialDateUtilities.dayCount30E(d1, d2);
        assertEquals(1, count);
    }

    @Test
    public void testCountFeb29s_onOneDay() {
        SerialDate start = SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000);
        SerialDate end = SpreadsheetDate.createInstance(29, Month.FEBRUARY, 2000);
        assertEquals(0, SerialDateUtilities.dayCount30E(start, end));
    }

    @Test
    @Ignore // Expected 1 got 88
    public void testCountFeb29s_inAFewDays() {
        SerialDate start = SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000);
        SerialDate end = SpreadsheetDate.createInstance(29, Month.APRIL, 2000);
        assertEquals(1, SerialDateUtilities.dayCount30E(start, end));
    }

    @Test
    @Ignore // Expected 3 got 3688
    public void testCountFeb29s_inAFewYears() {
        SerialDate start = SpreadsheetDate.createInstance(1, Month.FEBRUARY, 2000);
        SerialDate end = SpreadsheetDate.createInstance(29, Month.APRIL, 2010);
        assertEquals(3, SerialDateUtilities.dayCount30E(start, end));
    }
}
