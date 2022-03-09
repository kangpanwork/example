package com.sanri.test.sometest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class CalendarMain {
    @Test
    public void testAddDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -8);
        System.out.println(DateFormatUtils.format(cal, "yyyy-MM-dd"));
    }
}
