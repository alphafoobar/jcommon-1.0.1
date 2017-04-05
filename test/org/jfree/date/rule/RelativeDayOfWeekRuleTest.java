package org.jfree.date.rule;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RelativeDayOfWeekRuleTest {

    @Test
    public void cloneRuleTest() throws Exception {
        RelativeDayOfWeekRule rule = new RelativeDayOfWeekRule();
        RelativeDayOfWeekRule clone = (RelativeDayOfWeekRule) rule.clone();
        assertEquals(rule.getDateRule().getDate(1999), clone.getDateRule().getDate(1999));
    }

}