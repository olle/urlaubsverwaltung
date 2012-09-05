package org.synyx.urlaubsverwaltung.service.legacy;

import org.joda.time.DateMidnight;

import org.synyx.urlaubsverwaltung.domain.Application;
import org.synyx.urlaubsverwaltung.domain.ApplicationStatus;
import org.synyx.urlaubsverwaltung.domain.Person;

import java.math.BigDecimal;

import java.util.List;
import org.synyx.urlaubsverwaltung.service.OverlapCase;
import org.synyx.urlaubsverwaltung.service.OverlapCase;


/**
 * use this service to access to the application-data (who, how many days, ...)
 *
 * @author  Johannes Reuter
 * @author  Aljona Murygina
 */
public interface ApplicationService {
    
    
    /**
     * get Id of latest Application with given Person and ApplicationStatus
     * 
     * @param person
     * @param status
     * @return int id (primary key of Application)
     */
    int getIdOfLatestApplication(Person person, ApplicationStatus status);

    /**
     * use this to get an application by its id
     *
     * @param  id
     *
     * @return
     */
    Application getApplicationById(Integer id);


    /**
     * use this to save simple an edited application (e.g. after adding sick days)
     *
     * @param  application  the application to be saved
     */
    void simpleSave(Application application);


    /**
     * use this to save a new application, i.e. state is set to waiting and calculation (subtracting vacation days from
     * holidays account) is performed
     *
     * @param  application  the application to be saved
     */
    void save(Application application);


    /**
     * use this to set a application to allowed (only boss)
     *
     * @param  application  the application to be edited
     */
    void allow(Application application, Person boss);


    /**
     * use this to set a application to rejected (only boss)
     *
     * @param  application  the application to be edited
     */
    void reject(Application application, Person boss);


    /**
     * application's state is set to cancelled if user cancels vacation
     *
     * @param  application
     */
    void cancel(Application application);


    /**
     * This method calculates with the regular applications and the supplemental applications the number of days that
     * the given person has used for holidays in the given year.
     *
     * @param  person
     * @param  year
     *
     * @return  number of vacation days that the given person has used in the given year
     */
    BigDecimal getUsedVacationDaysOfPersonForYear(Person person, int year);


    
    /**
     * use this to get all applications by person and year (all applications no matter which status they have)
     *
     * @param  person
     * @param  year
     *
     * @return  return a list of applications of the given person and year
     */
    List<Application> getAllApplicationsByPersonAndYear(Person person, int year);



    /**
     * use this to get all applications by a certain state (like waiting) and year
     *
     * @param  state
     * @param  year
     *
     * @return  returns all applications in a list
     */
    List<Application> getApplicationsByStateAndYear(ApplicationStatus state, int year);

    
    /**
     * use this to get all cancelled applications that have have been allowed sometime (i.e. formerlyAllowed = true) by a certain year
     *
     * @param  year
     *
     * @return  returns all applications in a list
     */
    List<Application> getCancelledApplicationsByYearFormerlyAllowed(int year);
    
    

    /**
     * use this to get all applications with vacation time between startDate x and endDate y
     *
     * @param  startDate
     * @param  endDate
     *
     * @return
     */
    List<Application> getApplicationsForACertainTime(DateMidnight startDate, DateMidnight endDate);


    /**
     * signs an application with the private key of the signing user (applicant)
     *
     * @param  application
     * @param  user
     */
    void signApplicationByUser(Application application, Person user);


    /**
     * signs an application with the private key of the signing boss
     *
     * @param  application
     * @param  boss
     */
    void signApplicationByBoss(Application application, Person boss);
    
    /**
     * Needed for jmx demo: get all applications for leave in status waiting.
     * 
     * @return 
     */
    List<Application> getWaitingApplications();
    
    /**
     * Needed for jmx demo: get number of waiting applications.
     * 
     * @return 
     */
    long countWaitingApplications();
    
    /**
     * Needed for jmx demo: get number of applications with the given status.
     * 
     * @return
     */
    long countApplicationsInStatus(ApplicationStatus status);
    

    /**
     * check if application is valid and may be send to boss to be allowed or rejected or if person's leave account has
     * too little residual number of vacation days, so that taking holiday isn't possible
     *
     * @param  application
     *
     * @return  boolean: true if application is okay, false if there are too little residual number of vacation days
     */
    boolean checkApplication(Application application);


    /**
     * Check if new application is overlapping with an existent application. There are three possible cases: (1) The
     * period of the new application has no overlap at all with existent applications; i.e. you can calculate the normal
     * way and save the application if there are enough vacation days on person's holidays account. (2) The period of
     * the new application is element of an existent application's period; i.e. the new application is not necessary
     * because there is already an existent application for this period. (3) The period of the new application is part
     * of an existent application's period, but for a part of it you could apply new vacation; i.e. user must be asked
     * if he wants to apply for leave for the not overlapping period of the new application.
     *
     * @param  application  (the new application)
     *
     * @return  OverlapCase (Enum)
     */
    OverlapCase checkOverlap(Application application);
}