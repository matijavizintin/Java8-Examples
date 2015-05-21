package com.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by Matija Vižintin
 * Date: 05. 05. 2015
 * Time: 15.47
 */
public class NewTimeApiTest {

    @Before
    public void startup() {
        System.out.println("Test startup");
    }

    @After
    public void teardown() {
        System.out.println("Test teardown");
    }

    @Test
    public void test1() {
        Clock c = Clock.systemUTC();
        long ctm = System.currentTimeMillis();
        System.out.println("c = " + c.millis() + " ctm = " + ctm);

        ZoneId zoneId = ZoneId.of("Europe/Ljubljana");
        c = Clock.system(zoneId);
        System.out.println("c = " + c);

        LocalDateTime lt = LocalDateTime.now(zoneId);
        System.out.println("LT = " + lt.format(DateTimeFormatter.ISO_DATE_TIME));

        lt = lt.plus(5, ChronoUnit.MONTHS);
        System.out.println("LT = " + lt.format(DateTimeFormatter.ISO_DATE_TIME));

        LocalDateTime ldt = LocalDateTime.of(2015, Month.MARCH, 25, 0, 0);
        ZonedDateTime lj = ldt.atZone(zoneId);
        ZonedDateTime utc = ldt.atZone(ZoneId.of("UTC"));

        /*for (int i=0; i < 1000; i++) {
            lj = lj.plusHours(1);
            utc = utc.plusHours(1);
            System.out.println("LJ: " + lj.format(DateTimeFormatter.ISO_DATE_TIME));
            System.out.println("UTC: " + utc.format(DateTimeFormatter.ISO_DATE_TIME));
            System.out.println();
        }*/
    }

    @Test
    public void test2() {
        Clock clock = Clock.systemUTC();
        System.out.println("clock.instant() = " + clock.instant());
        System.out.println("clock.millis() = " + clock.millis());

        Instant instant = clock.instant();
        System.out.println("instant.atZone() = " + instant.atZone(ZoneId.of("Europe/Ljubljana")));
        System.out.println("instant.getEpochSecond() = " + instant.getEpochSecond());
        System.out.println("instant.getNano() = " + instant.getNano());

        LocalDate ld = LocalDate.now();
        LocalDate ldc = LocalDate.now(clock);
        System.out.println(ld);
        System.out.println(ldc);

        LocalTime lt = LocalTime.now();
        LocalTime ltc = LocalTime.now(ZoneId.of("UTC"));
        System.out.println(lt);
        System.out.println(ltc);

        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime ldtc = LocalDateTime.now(Clock.system(ZoneId.of("UTC")));
        System.out.println("ldt = " + ldt);
        System.out.println("ldtc = " + ldtc);
    }

    @Test
    public void test3() {
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now();
        ZonedDateTime zonedDateTime2 = ZonedDateTime.now(Clock.systemUTC());
        ZonedDateTime zonedDateTime3 = ZonedDateTime.now(ZoneId.of("Australia/Sydney"));
        ZonedDateTime zonedDateTime4 = ZonedDateTime.now(ZoneId.of("America/Buenos_Aires"));

        System.out.println(zonedDateTime1);
        System.out.println(zonedDateTime2);
        System.out.println(zonedDateTime3);
        System.out.println(zonedDateTime4);
    }

    @Test
    public void test4() {
        LocalDateTime ldt1 = LocalDateTime.of(2015, 1, 1, 1, 1);
        LocalDateTime ldt2 = LocalDateTime.of(2015, 2, 1, 1, 2);

        Duration duration = Duration.between(ldt1, ldt2);
        System.out.println("duration.getSeconds() = " + duration.getSeconds());
        System.out.println("duration.toDays = " + duration.toDays());

        long until1 = ldt1.until(ldt2, ChronoUnit.MINUTES);
        long until2 = ldt1.until(ldt2, ChronoUnit.SECONDS);
        long until3 = ldt1.until(ldt2, ChronoUnit.DAYS);
        System.out.println("until (m) = " + until1);
        System.out.println("until (s) = " + until2);
        System.out.println("until (d) = " + until3);
    }
}
