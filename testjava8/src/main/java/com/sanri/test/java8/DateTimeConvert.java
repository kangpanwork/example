package com.sanri.test.java8;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeConvert {
    final Date date = new Date();
    final Timestamp timestamp = new Timestamp(date.getTime());
    final Calendar calendar = Calendar.getInstance();
    final Instant instant = Instant.now();
    final LocalDateTime localDateTime = LocalDateTime.now();
    final ZonedDateTime zonedDateTime = ZonedDateTime.now();
    final LocalDate localDate = LocalDate.now();

    public void toInstant(){
        //Date转Instant
        Instant dateInstant = date.toInstant();
        //Timestamp转Instant
        Instant timestampInstant = timestamp.toInstant();
        //Calendar转Instant
        Instant calendarInstant = calendar.toInstant();
        //LocalDateTime转Instant
        Instant localDateTimeInstant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        //ZonedDateTime转Instant
        Instant zonedDateTimeInstant = zonedDateTime.toInstant();
        //LocalDate转Instant
        Instant localDateInstant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public void toLocalDateTime(){
        //Date转LocalDateTime
        LocalDateTime dateLocalDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        //Timestamp转LocalDateTime
        LocalDateTime timestampLocalDateTime = timestamp.toLocalDateTime();
        //Calendar转LocalDateTime
        LocalDateTime calendarLocalDateTime = LocalDateTime.ofInstant(calendar.toInstant(), ZoneOffset.systemDefault());
        //Instant转LocalDateTime
        LocalDateTime instantLocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        //ZonedDateTime转LocalDateTime
        LocalDateTime zonedDateTimeLocalDateTime = zonedDateTime.toLocalDateTime();
        //LocalDate转LocalDateTime
        LocalDateTime localDateLocalDateTime = localDate.atStartOfDay();
    }

    public void toZonedDateTime(){
        //Date转ZonedDateTime
        ZonedDateTime dateZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        //Timestamp转ZonedDateTime
        ZonedDateTime timestampZonedDateTime = ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        //Calendar转ZonedDateTime
        ZonedDateTime calendarZonedDateTime = ZonedDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
        //Instant转ZonedDateTime
        ZonedDateTime instantZonedDateTime = instant.atZone(ZoneId.systemDefault());
        //LocalDateTime转ZonedDateTime
        ZonedDateTime localDateTimeZonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        //LocalDate转ZonedDateTime
        ZonedDateTime localDateZonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
    }

    public void toLocalDate(){
        //Date转LocalDate
//        LocalDate dateLocalDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());  //jdk11
        LocalDate dateLocalDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
        //Timestamp转LocalDate
        LocalDate timestampLocalDate = timestamp.toLocalDateTime().toLocalDate();
        //Calendar转LocalDate
//        LocalDate calendarLocalDate = LocalDate.ofInstant(calendar.toInstant(), ZoneOffset.systemDefault());  //jdk11
        LocalDate calendarLocalDate = LocalDateTime.ofInstant(calendar.toInstant(), ZoneOffset.systemDefault()).toLocalDate();
        //Instant转LocalDate
//        LocalDate instantLocalDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());  //jdk11
        LocalDate instantLocalDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        //LocalDateTime转LocalDate
        LocalDate localDateTimeLocalDate = localDateTime.toLocalDate();
        //ZonedDateTime转LocalDate
        LocalDate zonedDateTimeLocalDate = zonedDateTime.toLocalDate();
    }

    public void toDate(){
        //Timestamp转Date
        Date timestampDate = new Date(timestamp.getTime());
        //Calendar转Date
        Date calendarDate = calendar.getTime();
        //Instant转Date
        Date instantDate = Date.from(instant);
        //LocalDateTime转Date
        Date localDateTimeDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        //ZonedDateTime转Date
        Date zonedDateTimeDate = Date.from(zonedDateTime.toInstant());
        //LocalDate转Date
        Date localDateDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 这里使用两种方法进行转化
     */
    @Test
    public void toDateDateTime(){
        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.JANUARY, 2, 14, 48, 25);
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        Date localDateTimeDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.format(localDateTimeDate));

        Date from = Date.from(instant);
        String format = DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.format(from);
        System.out.println(format);


    }

    public void toTimestamp(){
        //Date转Timestamp
        Timestamp dateTimestamp = new Timestamp(date.getTime());
        //Calendar转Timestamp
        Timestamp calendarTimestamp = new Timestamp(calendar.getTimeInMillis());
        //Instant转Timestamp
        Timestamp instantTimestamp = Timestamp.from(instant);
        //LocalDateTime转Timestamp
        Timestamp localDateTimeTimestamp = Timestamp.valueOf(localDateTime);
        //ZonedDateTime转Timestamp
        Timestamp zonedDateTimeTimestamp = Timestamp.from(zonedDateTime.toInstant());
        //LocalDate转Timestamp
        Timestamp localDateTimestamp = Timestamp.valueOf(localDate.atStartOfDay());
    }

    public void toCalendar(){
        //Date转Calendar
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        //Timestamp转Calendar
        Calendar timestampCalendar = Calendar.getInstance();
        timestampCalendar.setTimeInMillis(timestamp.getTime());
        //Instant转Calendar
        Calendar instantCalendar = GregorianCalendar.from(ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()));
        //LocalDateTime转Calendar
        Calendar localDateTimeCalendar = GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
        //ZonedDateTime转Calendar
        Calendar zonedDateTimeInstantCalendar = GregorianCalendar.from(zonedDateTime);
        //LocalDate转Calendar
        Calendar localDateCalendar = GregorianCalendar.from(localDate.atStartOfDay(ZoneId.systemDefault()));
    }


    public static void testMethod(){

    }
}
