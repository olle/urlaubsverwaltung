/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.synyx.urlaubsverwaltung.core.calendar;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import de.jollyday.Holiday;
import de.jollyday.HolidayManager;

import org.joda.time.DateMidnight;
import org.joda.time.chrono.GregorianChronology;

import org.springframework.stereotype.Component;

import org.synyx.urlaubsverwaltung.core.util.DateUtil;

import java.math.BigDecimal;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Service for calendar purpose using jollyday library.
 *
 * @author  Aljona Murygina
 */
@Component
public class JollydayCalendar {

    private static final double HALF_DAY = 0.5;
    private static final double FULL_DAY = 1.0;

    private HolidayManager manager;

    public JollydayCalendar() {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource("Holidays_custom.xml");

        manager = HolidayManager.getInstance(url);
    }

    /**
     * Returns all public holidays in a set that lie in the given date range.
     *
     * @param  startDate
     * @param  endDate
     *
     * @return
     */
    private Set<Holiday> getPublicHolidays(DateMidnight startDate, DateMidnight endDate) {

        // get all public holidays of this year
        Set<Holiday> holidays = manager.getHolidays(startDate.getYear());

        if (startDate.getYear() != endDate.getYear()) {
            Set<Holiday> holidaysNextYear = manager.getHolidays(endDate.getYear());
            holidays.addAll(holidaysNextYear);
        }

        return holidays;
    }


    /**
     * Checks if the given date is a public holiday by lookup in the given set of public holidays.
     *
     * @param  date
     *
     * @return  true if the given date is a public holiday, else false
     */
    boolean isPublicHoliday(DateMidnight date) {

        if (manager.isHoliday(date.toLocalDate())) {
            return true;
        }

        return false;
    }


    /**
     * Returns the working duration for a date: may be full day (1.0) for a non public holiday, half day (0.5) for
     * Christmas Eve or New Year's Eve or zero (0.0) for a public holiday.
     *
     * @param  date
     *
     * @return  working duration as BigDecimal: 1.0 for a non public holiday, 0.5 for Christmas Eve or New Year's Eve,
     *          0.0 for a public holiday
     */
    BigDecimal getWorkingDurationOfDate(DateMidnight date) {

        if (isPublicHoliday(date)) {
            if (DateUtil.isChristmasEveOrNewYearsEve(date)) {
                return new BigDecimal("0.5");
            } else {
                return BigDecimal.ZERO;
            }
        }

        return BigDecimal.ONE;
    }


    /**
     * Calculates number of public holidays between two given dates. If a public holiday is on Saturday or on Sunday it
     * is not counted among public holidays. Only public holidays on weekdays (Monday to Friday) are counted here.
     *
     * @param  start
     * @param  end
     *
     * @return  number of public holidays between startDate and endDate
     */
    double getNumberOfPublicHolidays(DateMidnight start, DateMidnight end) {

        DateMidnight startDate = start.withChronology(GregorianChronology.getInstance());
        DateMidnight endDate = end.withChronology(GregorianChronology.getInstance());

        double numberOfHolidays = 0.0;

        Set<Holiday> holidays = getPublicHolidays(startDate, endDate);

        // check if there are public holidays that are on Saturday or Sunday these days are worthless for the following
        // calculation

        // since it is not possible to remove elements during an iteration, you have to touch a Set<Holiday> that
        // contains all public holidays that are on weekdays

        Set<Holiday> holidaysOnWeekdays = getOnlyHolidaysOnWeekdays(holidays);

        // check if start date and end date are equal
        if (startDate.equals(endDate)) {
            numberOfHolidays = calculate(holidaysOnWeekdays, startDate);
        } else {
            DateMidnight date = startDate;

            // iteration while startdatum != enddatum
            // calculation for all dates between start date (inclusive start date) and end date (exclusive end date!)
            while (!date.isAfter(endDate)) {
                // check if current date is a public holiday
                numberOfHolidays += calculate(holidaysOnWeekdays, date);
                date = date.plusDays(1);
            }
        }

        return numberOfHolidays;
    }


    public Set<Holiday> getHolidays(int year) {

        return manager.getHolidays(year);
    }


    public Set<Holiday> getHolidays(int year, final int month) {

        Set<Holiday> holidays = getHolidays(year);

        Iterable<Holiday> holidaysForMonth = Iterables.filter(holidays, new Predicate<Holiday>() {

                    @Override
                    public boolean apply(Holiday holiday) {

                        return holiday.getDate().getMonthOfYear() == month;
                    }
                });

        return Sets.newHashSet(holidaysForMonth);
    }


    public List<String> getPublicHolidays(int year) {

        // get all public holidays of this year
        Collection<Holiday> holidays = manager.getHolidays(year);

        List<String> monthHolidays = new ArrayList<String>(holidays.size());

        for (Holiday holiday : holidays) {
            monthHolidays.add(holiday.getDate().toString());
        }

        return monthHolidays;
    }


    public List<String> getPublicHolidays(int year, int month) {

        // get all public holidays of this year
        Set<Holiday> holidays = manager.getHolidays(year);

        List<String> monthHolidays = new ArrayList<String>();

        for (Holiday holiday : holidays) {
            if (holiday.getDate().getMonthOfYear() == month) {
                monthHolidays.add(holiday.getDate().toString());
            }
        }

        return monthHolidays;
    }


    /**
     * a Set of a year's all public holidays is given, this method creates a Set of public holidays that only are on
     * weekdays.
     *
     * @param  holidays  Set of all public holidays of a year
     *
     * @return  a Set of public holidays that only are on weekdays
     */
    private Set<Holiday> getOnlyHolidaysOnWeekdays(Set<Holiday> holidays) {

        Set<Holiday> holidaysOnWeekdays = new HashSet<Holiday>();

        for (Holiday holiday : holidays) {
            if (DateUtil.isWorkDay(holiday.getDate().toDateMidnight())) {
                holidaysOnWeekdays.add(holiday);
            }
        }

        return holidaysOnWeekdays;
    }


    /**
     * calculates if a given date is a full public holiday (1.0), a half public holiday (0.5) or none (0.0).
     *
     * @param  holidaysOnWeekdays
     * @param  date
     *
     * @return
     */
    private double calculate(Set<Holiday> holidaysOnWeekdays, DateMidnight date) {

        double publicHolidays = 0.0;

        for (Holiday holiday : holidaysOnWeekdays) {
            // check if given date is a public holiday
            if (date.isEqual(holiday.getDate().toDateMidnight())) {
                // check if given date is Christmas Eve or New Year's Eve
                // because these ones are counted as 0.5 days
                if (DateUtil.isChristmasEveOrNewYearsEve(holiday.getDate().toDateMidnight())) {
                    publicHolidays += HALF_DAY;
                } else {
                    // else 1.0 days are counted
                    publicHolidays += FULL_DAY;
                }
            }
        }

        return publicHolidays;
    }
}
