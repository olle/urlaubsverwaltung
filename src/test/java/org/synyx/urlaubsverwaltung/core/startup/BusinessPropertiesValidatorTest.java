/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.synyx.urlaubsverwaltung.core.startup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import java.util.Properties;


/**
 * @author  Aljona Murygina
 */
public class BusinessPropertiesValidatorTest {

    private Runnable onError;
    private Properties properties;

    private BusinessPropertiesValidator propertiesValidator;

    @Before
    public void setUp() {

        onError = Mockito.mock(Runnable.class);
        properties = new Properties();

        propertiesValidator = new BusinessPropertiesValidator(onError, properties);
    }


    /* On error runnable tests */

    @Test
    public void ensureOnErrorRunnableIsCalledIfInvalidBusinessProperty() {

        properties.setProperty(BusinessPropertiesValidator.MAX_DAYS, "a");

        propertiesValidator.validateBusinessProperties();

        Mockito.verify(onError).run();
    }


    /* Validation of annual vacation property value */

    @Test
    public void ensureAnnualVacationPropertyMayNotBeAChar() {

        properties.setProperty(BusinessPropertiesValidator.MAX_DAYS, "a");

        Assert.assertFalse("Char is not a valid value for annual vacation property",
            propertiesValidator.isAnnualVacationPropertyValid());
    }


    @Test
    public void ensureAnnualVacationPropertyMayNotBeZero() {

        properties.setProperty(BusinessPropertiesValidator.MAX_DAYS, "0");

        Assert.assertFalse("Zero is not a valid value for annual vacation property",
            propertiesValidator.isAnnualVacationPropertyValid());
    }


    @Test
    public void ensureAnnualVacationPropertyMayNotBeNegative() {

        properties.setProperty(BusinessPropertiesValidator.MAX_DAYS, "-1");

        Assert.assertFalse("A negative number is not a valid value for annual vacation property",
            propertiesValidator.isAnnualVacationPropertyValid());
    }


    @Test
    public void ensureAnnualVacationPropertyHasAMaximumValue() {

        properties.setProperty(BusinessPropertiesValidator.MAX_DAYS, "367");

        Assert.assertFalse(
            "It is not valid to set the annual vacation property to more than number of days of one year",
            propertiesValidator.isAnnualVacationPropertyValid());
    }


    @Test
    public void ensureReturnsTrueIfAnnualVacationPropertyIsValid() {

        properties.setProperty(BusinessPropertiesValidator.MAX_DAYS, "366");

        Assert.assertTrue("Valid value for annual vacation property must return true",
            propertiesValidator.isAnnualVacationPropertyValid());
    }


    /* END: Validation of annual vacation property value */

    /* Validation of maximum months to apply for leave in advance property */

    @Test
    public void ensureMaximumMonthsToApplyForLeaveInAdvancePropertyMayNotBeAChar() {

        properties.setProperty(BusinessPropertiesValidator.MAX_MONTHS, "a");

        Assert.assertFalse("Char is not a valid value for maximum months to apply for leave in advance property",
            propertiesValidator.isMaximumMonthsToApplyForLeaveInAdvancePropertyValid());
    }


    @Test
    public void ensureMaximumMonthsToApplyForLeaveInAdvancePropertyMayNotBeZero() {

        properties.setProperty(BusinessPropertiesValidator.MAX_MONTHS, "0");

        Assert.assertFalse("Zero is not a valid value for maximum months to apply for leave in advance property",
            propertiesValidator.isMaximumMonthsToApplyForLeaveInAdvancePropertyValid());
    }


    @Test
    public void ensureMaximumMonthsToApplyForLeaveInAdvancePropertyMayNotBeNegative() {

        properties.setProperty(BusinessPropertiesValidator.MAX_MONTHS, "-1");

        Assert.assertFalse(
            "A negative number is not a valid value for maximum months to apply for leave in advance property",
            propertiesValidator.isMaximumMonthsToApplyForLeaveInAdvancePropertyValid());
    }


    @Test
    public void ensureMaximumMonthsToApplyForLeaveInAdvancePropertyHasAMaximumValue() {

        properties.setProperty(BusinessPropertiesValidator.MAX_MONTHS, "37");

        Assert.assertFalse("More than three years in advance it is not realistic to apply for leave",
            propertiesValidator.isMaximumMonthsToApplyForLeaveInAdvancePropertyValid());
    }


    @Test
    public void ensureMaximumMonthsToApplyForLeaveInAdvancePropertyMayNotBeADecimalNumber() {

        properties.setProperty(BusinessPropertiesValidator.MAX_MONTHS, "12.5");

        Assert.assertFalse(
            "A decimal number is not a valid value for maximum months to apply for leave in advance property",
            propertiesValidator.isMaximumMonthsToApplyForLeaveInAdvancePropertyValid());

        properties.setProperty(BusinessPropertiesValidator.MAX_MONTHS, "12,5");

        Assert.assertFalse(
            "A decimal number is not a valid value for maximum months to apply for leave in advance property",
            propertiesValidator.isMaximumMonthsToApplyForLeaveInAdvancePropertyValid());
    }


    @Test
    public void ensureReturnsTrueIfMaximumMonthsToApplyForLeaveInAdvancePropertyIsValid() {

        properties.setProperty(BusinessPropertiesValidator.MAX_DAYS, "12");

        Assert.assertTrue("Valid value for maximum months to apply for leave in advance property must return true",
            propertiesValidator.isAnnualVacationPropertyValid());
    }
}
