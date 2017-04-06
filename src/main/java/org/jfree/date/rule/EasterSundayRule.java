/* (C) Copyright 2000-2005, by Object Refinery Limited.
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

package org.jfree.date.rule;

import org.jfree.date.SerialDate;
import org.jfree.date.SpreadsheetDate;

/**
 * An annual date rule for Easter (Sunday).  The algorithm used here was
 * obtained from a Calendar FAQ which can be found at:
 * <P>
 * <a href="http://www.tondering.dk/claus/calendar.html"
 *     >http://www.tondering.dk/claus/calendar.html</a>.
 * <P>
 * It is based on an algorithm by Oudin (1940) and quoted in "Explanatory Supplement to the
 * Astronomical Almanac", P. Kenneth Seidelmann, editor.
 *
 * @author David Gilbert
 */
public class EasterSundayRule extends AnnualDateRule {

    /**
     * Returns the date of Easter Sunday for the given year.  See the class
     * description for the source of the algorithm.
     * <P>
     * This method supports the AnnualDateRule interface.
     *
     * @param year  the year to check.
     *
     * @return the date of Easter Sunday for the given year.
     */
    public SerialDate getDate(int year) {
        final int g = year % 19;
        final int c = year / 100;
        final int h = (c - c / 4 - (8 * c + 13) / 25 + 19 * g + 15) % 30;
        final int i = h - h / 28 * (1 - h / 28 * 29 / (h + 1) * (21 - g) / 11);
        final int j = (year + year / 4 + i + 2 - c + c / 4) % 7;
        final int l = i - j;
        final int month = 3 + (l + 40) / 44;
        final int day = l + 28 - 31 * (month / 4);
        return SpreadsheetDate.createInstance(day, month, year);
    }

}
