/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.option;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.opengamma.analytics.financial.equity.EqyOptBaroneAdesiWhaleyGreekCalculator;
import com.opengamma.analytics.financial.equity.StaticReplicationDataBundle;
import com.opengamma.analytics.financial.greeks.Greek;
import com.opengamma.analytics.financial.greeks.GreekResultCollection;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;

/**
 *
 */
public class EquityOptionBAWGreeksFunction extends EquityOptionBAWFunction {
  /** Value requirement names */
  private static final String[] GREEK_NAMES = new String[] {
    ValueRequirementNames.DELTA,
    ValueRequirementNames.DUAL_DELTA,
    ValueRequirementNames.RHO,
    ValueRequirementNames.CARRY_RHO,
    ValueRequirementNames.VEGA,
    ValueRequirementNames.THETA,
    ValueRequirementNames.GAMMA
  };
  /** Equivalent greeks */
  private static final Greek[] GREEKS = new Greek[] {
    Greek.DELTA,
    Greek.DUAL_DELTA,
    Greek.RHO,
    Greek.CARRY_RHO,
    Greek.VEGA,
    Greek.THETA,
    Greek.GAMMA
  };

  /**
   * Default constructor
   */
  public EquityOptionBAWGreeksFunction() {
    super(GREEK_NAMES);
  }

  @Override
  protected Set<ComputedValue> computeValues(final InstrumentDerivative derivative, final StaticReplicationDataBundle market, final FunctionInputs inputs,
      final Set<ValueRequirement> desiredValues, final ComputationTargetSpecification targetSpec, final ValueProperties resultProperties) {
    final GreekResultCollection greeks = derivative.accept(EqyOptBaroneAdesiWhaleyGreekCalculator.getInstance(), market);
    final Set<ComputedValue> result = new HashSet<>();
    for (int i = 0; i < GREEKS.length; i++) {
      final ValueSpecification spec = new ValueSpecification(GREEK_NAMES[i], targetSpec, resultProperties);
      final double greek = greeks.get(GREEKS[i]);
      result.add(new ComputedValue(spec, greek));
    }
    return result;
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target, final Map<ValueSpecification, ValueRequirement> inputs) {
    final Set<ValueSpecification> results = super.getResults(context, target, inputs);
    final Set<ValueSpecification> resultsWithoutCurrency = Sets.newHashSetWithExpectedSize(results.size());
    for (final ValueSpecification spec : results) {
      final String name = spec.getValueName();
      final ComputationTargetSpecification targetSpec = spec.getTargetSpecification();
      final ValueProperties properties = spec.getProperties().copy()
          .withoutAny(ValuePropertyNames.CURRENCY)
          .get();
      resultsWithoutCurrency.add(new ValueSpecification(name, targetSpec, properties));
    }
    return results;
  }
}
