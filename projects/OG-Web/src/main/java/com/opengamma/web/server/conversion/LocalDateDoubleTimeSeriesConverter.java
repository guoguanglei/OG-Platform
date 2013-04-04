/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.web.server.conversion;

import java.util.HashMap;
import java.util.Map;

import org.threeten.bp.ZonedDateTime;

import com.google.common.collect.ImmutableMap;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.timeseries.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.timeseries.zoneddatetime.ZonedDateTimeDoubleTimeSeries;

/**
 * Converter for {@link LocalDateDoubleTimeSeries} results.
 */
public class LocalDateDoubleTimeSeriesConverter implements ResultConverter<LocalDateDoubleTimeSeries> {

  /* package */static Object convertForDisplayImpl(ResultConverterCache context, ValueSpecification valueSpec, LocalDateDoubleTimeSeries value, ConversionMode mode) {
    Map<String, Object> result = new HashMap<String, Object>();
    if (value.isEmpty()) {
      return result;
    }
    Map<String, Object> summary = ImmutableMap.<String, Object>of(
        "from", value.getEarliestTime().toString(),
        "to", value.getLatestTime().toString());
    result.put("summary", summary);
    if (mode == ConversionMode.FULL) {
      ZonedDateTimeDoubleTimeSeries zonedTimeSeries = value.toZonedDateTimeDoubleTimeSeries();
      Object[] tsData = new Object[zonedTimeSeries.size()];
      for (int i = 0; i < zonedTimeSeries.size(); i++) {
        ZonedDateTime time = zonedTimeSeries.getTimeAtIndex(i);
        double tsValue = zonedTimeSeries.getValueAtIndex(i);
        tsData[i] = new Object[] {time.toInstant().toEpochMilli(), tsValue};
      }
      Map<String, Object> ts = ImmutableMap.<String, Object>of(
          "template_data", ImmutableMap.<String, Object>of(
              "data_field", valueSpec.getValueName(),
              "observation_time", valueSpec.getValueName()),
          "timeseries", ImmutableMap.<String, Object>of(
              "fieldLabels", new String[] {"Time", "Value"},
              "data", tsData));
      result.put("ts", ts);
    }
    return result;
  }

  @Override
  public Object convertForDisplay(ResultConverterCache context, ValueSpecification valueSpec, LocalDateDoubleTimeSeries value, ConversionMode mode) {
    return convertForDisplayImpl(context, valueSpec, value, mode);
  }

  @Override
  public Object convertForHistory(ResultConverterCache context, ValueSpecification valueSpec, LocalDateDoubleTimeSeries value) {
    return null;
  }

  @Override
  public String convertToText(ResultConverterCache context, ValueSpecification valueSpec, LocalDateDoubleTimeSeries value) {
    return value.toString();
  }

  @Override
  public String getFormatterName() {
    return "TIME_SERIES";
  }

}
