/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util.timeseries;

import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;
import com.opengamma.util.timeseries.date.time.DateTimeDoubleTimeSeries;
import com.opengamma.util.timeseries.date.time.ListDateTimeDoubleTimeSeries;

@Test(groups = TestGroup.UNIT)
public class ListDateTimeDoubleTimeSeriesTest extends DateDoubleTimeSeriesTest {

  @Override
  protected DateTimeDoubleTimeSeries createEmptyTimeSeries() {
    return new ListDateTimeDoubleTimeSeries();
  }

  @Override
  protected DateTimeDoubleTimeSeries createTimeSeries(Date[] times, double[] values) {
    return new ListDateTimeDoubleTimeSeries(times, values);
  }

  @Override
  protected DateTimeDoubleTimeSeries createTimeSeries(List<Date> times, List<Double> values) {
    return new ListDateTimeDoubleTimeSeries(times, values);
  }

  @Override
  protected DateTimeDoubleTimeSeries createTimeSeries(DoubleTimeSeries<Date> dts) {
    return new ListDateTimeDoubleTimeSeries(dts);
  }

}
