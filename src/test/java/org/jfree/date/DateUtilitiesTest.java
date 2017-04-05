package org.jfree.date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Calendar;
import org.junit.Test;

public class DateUtilitiesTest {

    @Test
    public void createDate() throws Exception {
        Calendar calendar = createCalendar(2017, Calendar.APRIL, 7);
        assertThat(DateUtilities.createDate(2017, MonthConstants.APRIL, 7),
            equalTo(calendar.getTime()));
    }

    @Test
    public void createDate_withTime() throws Exception {
        Calendar calendar = createCalendar(2017, Calendar.APRIL, 7);
        assertThat(DateUtilities.createDate(2017, MonthConstants.APRIL, 7, 0, 0),
            equalTo(calendar.getTime()));
    }

    @Test
    public void createDate_fromSpreadsheetDate() throws Exception {
        Calendar calendar = createCalendar(2017, Calendar.APRIL, 7);
        assertThat(DateUtilities
                .createDateFromSerialDate(SerialDate.createInstance(7, MonthConstants.APRIL, 2017)),
            equalTo(calendar.getTime()));
    }

    private Calendar createCalendar(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, date, 0, 0, 0);
        return calendar;
    }
}