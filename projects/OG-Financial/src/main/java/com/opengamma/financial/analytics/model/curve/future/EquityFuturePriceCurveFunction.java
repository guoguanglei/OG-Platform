/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.curve.future;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Clock;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import com.google.common.collect.Sets;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.math.curve.NodalDoublesCurve;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.CompiledFunctionDefinition;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.analytics.model.FutureOptionExpiries;
import com.opengamma.financial.analytics.model.InstrumentTypeProperties;
import com.opengamma.financial.analytics.model.equity.EquitySecurityUtils;
import com.opengamma.financial.analytics.volatility.surface.ConfigDBFuturePriceCurveDefinitionSource;
import com.opengamma.financial.analytics.volatility.surface.ConfigDBFuturePriceCurveSpecificationSource;
import com.opengamma.financial.analytics.volatility.surface.FuturePriceCurveDefinition;
import com.opengamma.financial.analytics.volatility.surface.FuturePriceCurveInstrumentProvider;
import com.opengamma.financial.analytics.volatility.surface.FuturePriceCurveSpecification;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.id.ExternalId;

//TODO: Take account of holidays
//TODO: Modify parent class to handle most of this logic
//FIXME: Hardcoded to BBG
//TODO: Take expiry rules from instrument provider

/**
 * Function providing an equity future curve.
 *
 * Currently only works for Bloomberg. Need to remove hardcoded Curve properties when expanding to other data providers
 */
public class EquityFuturePriceCurveFunction extends FuturePriceCurveFunction {

  private static final Logger s_logger = LoggerFactory.getLogger(FuturePriceCurveFunction.class);

  @Override
  protected String getInstrumentType() {
    return InstrumentTypeProperties.EQUITY_FUTURE_PRICE;
  }

  private FuturePriceCurveDefinition<Object> getCurveDefinition(final ConfigDBFuturePriceCurveDefinitionSource source, final ComputationTarget target,
      final String definitionName) {
    final String fullDefinitionName = definitionName + "_" + EquitySecurityUtils.getIndexOrEquityName(target.getUniqueId());
    return (FuturePriceCurveDefinition<Object>) source.getDefinition(fullDefinitionName, getInstrumentType());
  }

  private FuturePriceCurveSpecification getCurveSpecification(final ConfigDBFuturePriceCurveSpecificationSource source, final ComputationTarget target,
      final String specificationName) {
    final String fullSpecificationName = specificationName + "_" + EquitySecurityUtils.getIndexOrEquityName(target.getUniqueId());
    return source.getSpecification(fullSpecificationName, getInstrumentType());
  }

  public static Set<ValueRequirement> buildRequirements(final FuturePriceCurveSpecification futurePriceCurveSpecification, final FuturePriceCurveDefinition<Object> futurePriceCurveDefinition,
      final ZonedDateTime atInstant) {
    final Set<ValueRequirement> result = new HashSet<>();
    final FuturePriceCurveInstrumentProvider<Object> futurePriceCurveProvider = (FuturePriceCurveInstrumentProvider<Object>) futurePriceCurveSpecification.getCurveInstrumentProvider();
    for (final Object x : futurePriceCurveDefinition.getXs()) {
      final ExternalId identifier = futurePriceCurveProvider.getInstrument(x, atInstant.getDate());
      result.add(new ValueRequirement(futurePriceCurveProvider.getDataFieldName(), ComputationTargetType.PRIMITIVE, identifier));
    }
    return result;
  }

  @Override
  protected Double getTimeToMaturity(int n, LocalDate date, Calendar calendar) {
    //TODO: Need to check for specific expiry rule from instrument provider
    return Double.valueOf(TimeCalculator.getTimeBetween(date, FutureOptionExpiries.EQUITY.getOneChicagoEquityFutureExpiry(n, date)));
  }

  @Override
  public CompiledFunctionDefinition compile(final FunctionCompilationContext context, final Instant atInstant) {
    final ZonedDateTime atZDT = ZonedDateTime.ofInstant(atInstant, ZoneOffset.UTC);
    final ConfigSource configSource = OpenGammaCompilationContext.getConfigSource(context);
    final ConfigDBFuturePriceCurveDefinitionSource curveDefinitionSource = new ConfigDBFuturePriceCurveDefinitionSource(configSource);
    final ConfigDBFuturePriceCurveSpecificationSource curveSpecificationSource = new ConfigDBFuturePriceCurveSpecificationSource(configSource);
    return new AbstractInvokingCompiledFunction(atZDT.with(LocalTime.MIDNIGHT), atZDT.plusDays(1).with(LocalTime.MIDNIGHT).minusNanos(1000000)) {

      @Override
      public ComputationTargetType getTargetType() {
        return ComputationTargetType.PRIMITIVE;
      }

      @SuppressWarnings("synthetic-access")
      @Override
      public Set<ValueSpecification> getResults(final FunctionCompilationContext myContext, final ComputationTarget target) {
        final ValueProperties curveProperties = createValueProperties()
            .withAny(ValuePropertyNames.CURVE)
            .with(InstrumentTypeProperties.PROPERTY_SURFACE_INSTRUMENT_TYPE, getInstrumentType()).get();
        final ValueSpecification futurePriceCurveResult = new ValueSpecification(ValueRequirementNames.FUTURE_PRICE_CURVE_DATA,
            target.toSpecification(),
            curveProperties);
        return Collections.singleton(futurePriceCurveResult);
      }

      @SuppressWarnings("synthetic-access")
      @Override
      public Set<ValueRequirement> getRequirements(final FunctionCompilationContext myContext, final ComputationTarget target, final ValueRequirement desiredValue) {
        final ValueProperties constraints = desiredValue.getConstraints();
        final String curveName;
        final Set<String> curveNames = constraints.getValues(ValuePropertyNames.CURVE);
        if (curveNames == null || curveNames.size() != 1) {
          s_logger.error("Can only get a single curve; asked for " + curveNames);
          return null;
        }
        curveName = curveNames.iterator().next();
        //TODO use separate definition and specification names?
        final String curveDefinitionName = "BBG";//curveName;
        final String curveSpecificationName = "BBG";//curveName;
        final FuturePriceCurveDefinition<Object> priceCurveDefinition = getCurveDefinition(curveDefinitionSource, target,
            curveDefinitionName);
        if (priceCurveDefinition == null) {
          s_logger.error("Price curve definition for target {} with curve name {} and instrument type {} was null", new Object[] { target, curveDefinitionName, getInstrumentType() });
          return null;
        }
        final FuturePriceCurveSpecification priceCurveSpecification = getCurveSpecification(curveSpecificationSource, target,
            curveSpecificationName);
        if (priceCurveSpecification == null) {
          s_logger.error("Price curve specification for target {} with curve name {} and instrument type {} was null", new Object[] { target, curveSpecificationName, getInstrumentType() });
          return null;
        }
        final Set<ValueRequirement> requirements = Collections.unmodifiableSet(buildRequirements(priceCurveSpecification, priceCurveDefinition, atZDT));
        return requirements;
      }

      @Override
      public boolean canHandleMissingInputs() {
        return true;
      }

      @Override
      public boolean canHandleMissingRequirements() {
        return true;
      }

      @SuppressWarnings({ "synthetic-access" })
      @Override
      public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target,
          final Set<ValueRequirement> desiredValues) {
        final ValueRequirement desiredValue = desiredValues.iterator().next();
        final String curveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
        //TODO use separate definition and specification names?
        final String curveDefinitionName = "BBG"; //curveName;
        final String curveSpecificationName = "BBG"; //curveName;
        final FuturePriceCurveDefinition<Object> priceCurveDefinition = getCurveDefinition(curveDefinitionSource, target, curveDefinitionName);
        final FuturePriceCurveSpecification priceCurveSpecification = getCurveSpecification(curveSpecificationSource, target, curveSpecificationName);
        final Clock snapshotClock = executionContext.getValuationClock();
        final ZonedDateTime now = ZonedDateTime.now(snapshotClock);
        final DoubleArrayList xList = new DoubleArrayList();
        final DoubleArrayList prices = new DoubleArrayList();
        final FuturePriceCurveInstrumentProvider<Number> futurePriceCurveProvider = (FuturePriceCurveInstrumentProvider<Number>) priceCurveSpecification.getCurveInstrumentProvider();
        final LocalDate valDate = now.getDate();
        if (inputs.getAllValues().isEmpty()) {
          throw new OpenGammaRuntimeException("Could not get any data for future price curve called " + curveSpecificationName);
        }
        for (final Object x : priceCurveDefinition.getXs()) {
          final Number xNum = (Number) x;
          final ExternalId identifier = futurePriceCurveProvider.getInstrument(xNum, valDate);
          final ValueRequirement requirement = new ValueRequirement(futurePriceCurveProvider.getDataFieldName(), ComputationTargetType.PRIMITIVE, identifier);
          Double futurePrice = null;
          if (inputs.getValue(requirement) != null) {
            futurePrice = (Double) inputs.getValue(requirement);
            if (futurePrice != null) {
              final Double ttm = getTimeToMaturity(xNum.intValue(), valDate, null);
              xList.add(ttm);
              prices.add(futurePrice);
            }
          }
        }
        final ValueSpecification futurePriceCurveResult = new ValueSpecification(ValueRequirementNames.FUTURE_PRICE_CURVE_DATA,
            target.toSpecification(),
            createValueProperties()
                .with(ValuePropertyNames.CURVE, curveName)
                .with(InstrumentTypeProperties.PROPERTY_SURFACE_INSTRUMENT_TYPE, getInstrumentType()).get());
        final NodalDoublesCurve curve = NodalDoublesCurve.from(xList.toDoubleArray(), prices.toDoubleArray());
        final ComputedValue futurePriceCurveResultValue = new ComputedValue(futurePriceCurveResult, curve);
        return Sets.newHashSet(futurePriceCurveResultValue);
      }

    };
  }

}
