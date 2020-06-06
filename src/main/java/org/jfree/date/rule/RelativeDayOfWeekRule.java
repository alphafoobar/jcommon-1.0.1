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
 * --------------------------
 * RelativeDayOfWeekRule.java
 * --------------------------
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: RelativeDayOfWeekRule.java,v 1.6 2005/11/16 15:58:40 taqua Exp $
 *
 * Changes (from 26-Oct-2001)
 * --------------------------
 * 26-Oct-2001 : Changed package to com.jrefinery.date.*;
 * 03-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 *
 */

package org.jfree.date.rule;

import static org.jfree.date.SerialDate.FOLLOWING;
import static org.jfree.date.SerialDate.NEAREST;
import static org.jfree.date.SerialDate.PRECEDING;
import static org.jfree.date.SerialDateUtilities.checkValidYear;

import org.jfree.date.SerialDate;

/**
 * An annual date rule that returns a date for each year based on (a) a
 * reference rule; (b) a day of the week; and (c) a selection parameter
 * (SerialDate.PRECEDING, SerialDate.NEAREST, SerialDate.FOLLOWING).
 * <P>
 * For example, Good Friday can be specified as 'the Friday PRECEDING Easter
 * Sunday'.
 *
 * @author David Gilbert
 */
public class RelativeDayOfWeekRule extends AnnualDateRule {

    private final AnnualDateRule dateRule;
    private final int dayOfWeek;
    private final int relative;

    /**
     * Standard constructor - builds rule based on the supplied sub-rule.
     *
     * @param subRule the rule that determines the reference date.
     * @param dayOfWeek the day-of-the-week relative to the reference date.
     * @param relative indicates *which* day-of-the-week (preceding, nearest or following).
     */
    public RelativeDayOfWeekRule(AnnualDateRule subRule, int dayOfWeek, int relative) {
        this.dateRule = subRule;
        this.dayOfWeek = dayOfWeek;
        this.relative = relative;
    }

    /**
     * Returns the sub-rule (also called the reference rule).
     *
     * @return The annual date rule that determines the reference date for this rule.
     */
    public AnnualDateRule getDateRule() {
        return this.dateRule;
    }

    /**
     * Returns the day-of-the-week for this rule.
     *
     * @return the day-of-the-week for this rule.
     */
    public int getDayOfWeek() {
        return this.dayOfWeek;
    }

    /**
     * Returns the 'relative' attribute, that determines *which*
     * day-of-the-week we are interested in (SerialDate.PRECEDING,
     * SerialDate.NEAREST or SerialDate.FOLLOWING).
     *
     * @return The 'relative' attribute.
     */
    public int getRelative() {
        return this.relative;
    }

    /**
     * Returns the date generated by this rule, for the specified year.
     *
     * @param year the year (1900 &lt;= year &lt;= 9999).
     * @return The date generated by the rule for the given year (possibly <code>null</code>).
     */
    public SerialDate getDate(int year) {
        checkValidYear(year);

        // calculate the date...
        return switch (this.relative) {
            case (PRECEDING) -> base(year).getPreviousDayOfWeek(this.dayOfWeek);
            case (NEAREST) -> base(year).getNearestDayOfWeek(this.dayOfWeek);
            case (FOLLOWING) -> base(year).getFollowingDayOfWeek(this.dayOfWeek);
            default -> base(year);
        };
    }

    private SerialDate base(int year) {
        return this.dateRule.getDate(year);
    }
}
