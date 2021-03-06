/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.interestrate.bond.definition;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import org.testng.annotations.Test;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.financial.convention.yield.YieldConvention;
import com.opengamma.financial.convention.yield.YieldConventionFactory;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtils;

/**
 * Tests related to the construction of bills security.
 * @deprecated This class tests deprecated functionality
 */
@Deprecated
public class DeprecatedBillSecurityTest {

  private final static Currency EUR = Currency.EUR;
  private static final Calendar CALENDAR = new MondayToFridayCalendar("TARGET");
  private final static ZonedDateTime REFERENCE_DATE = DateUtils.getUTCDate(2012, 1, 16);

  private static final DayCount ACT360 = DayCountFactory.INSTANCE.getDayCount("Actual/360");
  private static final int SETTLEMENT_DAYS = 2;
  private static final YieldConvention YIELD_CONVENTION = YieldConventionFactory.INSTANCE.getYieldConvention("INTEREST@MTY");

  private final static String ISSUER_BEL = "BELGIUM GOVT";
  private final static String ISSUER_GER = "GERMANY GOVT";
  private final static ZonedDateTime END_DATE = DateUtils.getUTCDate(2012, 2, 29);
  private final static double NOTIONAL = 1000;

  private final static ZonedDateTime SETTLE_DATE = ScheduleCalculator.getAdjustedDate(REFERENCE_DATE, SETTLEMENT_DAYS, CALENDAR);
  private final static String DSC_NAME = "EUR Discounting";
  private final static String CREDIT_NAME = "EUR BELGIUM GOVT";
  private final static double SETTLE_TIME = TimeCalculator.getTimeBetween(REFERENCE_DATE, SETTLE_DATE);
  private final static double END_TIME = TimeCalculator.getTimeBetween(REFERENCE_DATE, END_DATE);
  private final static double ACCRUAL_FACTOR = ACT360.getDayCountFraction(SETTLE_DATE, END_DATE);
  private final static BillSecurity BILL_SEC = new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullCurrency() {
    new BillSecurity(null, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullYield() {
    new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, null, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullISSUEUR() {
    new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, null, CREDIT_NAME, DSC_NAME);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullDsc() {
    new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void notionalPositive() {
    new BillSecurity(EUR, SETTLE_TIME, END_TIME, -NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void times() {
    new BillSecurity(EUR, END_TIME, SETTLE_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
  }

  @Test
  /**
   * Tests the bill getters.
   */
  public void getters() {
    assertEquals("Bill Security: getter", EUR, BILL_SEC.getCurrency());
    assertEquals("Bill Security: getter", SETTLE_TIME, BILL_SEC.getSettlementTime());
    assertEquals("Bill Security: getter", END_TIME, BILL_SEC.getEndTime());
    assertEquals("Bill Security: getter", NOTIONAL, BILL_SEC.getNotional());
    assertEquals("Bill Security: getter", YIELD_CONVENTION, BILL_SEC.getYieldConvention());
    assertEquals("Bill Security: getter", ACCRUAL_FACTOR, BILL_SEC.getAccrualFactor());
    assertEquals("Bill Security: getter", ISSUER_BEL, BILL_SEC.getIssuer());
    assertEquals("Bill Security: getter", DSC_NAME, BILL_SEC.getDiscountingCurveName());
  }

  @Test
  /**
   * Tests the equal and hash-code methods.
   */
  public void equalHash() {
    assertEquals("Bill Security: equal-hash code", BILL_SEC, BILL_SEC);
    final BillSecurity other = new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
    assertEquals("Bill Security: equal-hash code", BILL_SEC, other);
    assertEquals("Bill Security: equal-hash code", BILL_SEC.hashCode(), other.hashCode());
    BillSecurity modified;
    modified = new BillSecurity(Currency.USD, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
    modified = new BillSecurity(EUR, SETTLE_TIME + 0.01, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
    modified = new BillSecurity(EUR, SETTLE_TIME, END_TIME + 0.01, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
    modified = new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL + 1.0, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
    modified = new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YieldConventionFactory.INSTANCE.getYieldConvention("DISCOUNT"), ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
    modified = new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR + 0.05, ISSUER_BEL, CREDIT_NAME, DSC_NAME);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
    modified = new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_GER, CREDIT_NAME, DSC_NAME);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
    modified = new BillSecurity(EUR, SETTLE_TIME, END_TIME, NOTIONAL, YIELD_CONVENTION, ACCRUAL_FACTOR, ISSUER_BEL, CREDIT_NAME, ISSUER_BEL);
    assertFalse("Bill Security: equal-hash code", BILL_SEC.equals(modified));
  }

}
