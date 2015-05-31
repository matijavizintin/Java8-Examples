package com.test;

import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Matija Vižintin
 * Date: 31. 05. 2015
 * Time: 17.10
 */
public class TimeTest extends LoggingTimedTest {

    /**
     * Simple clock test. This test should execute in less than 1ms.
     */
    @Test
    public void clock() {
        // current instant
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();       // clock can be used instead of System.currentTimeMillis()
        long millis2 = System.currentTimeMillis();
        Assert.assertEquals(millis, millis2);

        // instant
        Instant instant = clock.instant();
        long millis3 = instant.toEpochMilli();
        Assert.assertEquals(millis, millis3);

        // date is now legacy
        Date date = Date.from(instant);
        Assert.assertEquals(millis, date.getTime());
    }

    /**
     * Simple time zone test. It shows interoperability between old TimeZone and new ZoneId.
     */
    @Test
    public void timeZone() {
        // local time zone
        ZoneId europeLjubljana = ZoneId.of("Europe/Ljubljana");

        // create timezone object - NOTE: timeZone is old date API
        TimeZone timeZone1 = TimeZone.getTimeZone(europeLjubljana);
        TimeZone timeZone2 = TimeZone.getTimeZone("Europe/Ljubljana");
        Assert.assertEquals(timeZone1, timeZone2);

        // create ZoneId from timeZone
        ZoneId europeLjubljana2 = ZoneId.of(timeZone2.getID());
        Assert.assertEquals(europeLjubljana, europeLjubljana2);

        // get rules and assert offset
        ZoneRules zoneRules = europeLjubljana.getRules();
        ZoneOffset zoneOffset = zoneRules.getOffset((LocalDateTime.of(2015, Month.JANUARY, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)));
        int offset = zoneOffset.get(ChronoField.OFFSET_SECONDS);
        Assert.assertEquals(3600, offset);
    }

    /**
     * Simple local time test. It shows how local time is created and some of local time capabilities.
     */
    @Test
    public void localTime() {
        ZoneId zone1 = ZoneId.of("Europe/London");
        ZoneId zone2 = ZoneId.of("Europe/Ljubljana");
        Locale locale = new Locale("sl", "si");

        // local time
        LocalTime localTime1 = LocalTime.now(zone1);
        LocalTime localTime2 = LocalTime.now(zone2);
        long difference = ChronoUnit.HOURS.between(localTime1, localTime2);

        // assert local time
        Assert.assertTrue(localTime1.isBefore(localTime2));
        Assert.assertEquals(1, difference);

        // local time parser
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale);  // immutable and thread-safe
        LocalTime localTime3 = LocalTime.parse("11:11", dateTimeFormatter);
        Assert.assertEquals(LocalTime.of(11, 11), localTime3);

        // local time in immutable
        LocalTime now = LocalTime.now();
        LocalTime advanced = now.plus(1, ChronoUnit.HOURS);
        long difference2 = ChronoUnit.HOURS.between(now, advanced);

        // assert immutable
        Assert.assertNotEquals(now, advanced);
        Assert.assertEquals(1, difference2);

        // to instance
        Instant instant = LocalDateTime.now().atZone(zone2).toInstant();
        Date date = Date.from(instant);
    }

    /**
     * LocalDate and LocalDateTime works exactly the same (obviously one is for date, the other combines date and time) as LocalTime so there are no
     * examples for them.
     */
    @Test
    public void localDateTime() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
    }

}
