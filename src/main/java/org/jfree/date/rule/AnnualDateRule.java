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

/**
 * The base class for all 'annual' date rules: that is, rules for generating
 * one date for any given year.
 * <P>
 * One example is Easter Sunday (which can be calculated using published algorithms).
 *
 * @author David Gilbert
 */
public abstract class AnnualDateRule {

    /**
     * Returns the date for this rule, given the year.
     *
     * @param year  the year (1900 &lt;= year &lt;= 9999).
     *
     * @return the date for this rule, given the year.
     */
    public abstract SerialDate getDate(int year);

}
