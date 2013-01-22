/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.volatility.surface.black.defaultproperties;

import java.util.Collections;
import java.util.Set;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.financial.analytics.OpenGammaFunctionExclusions;
import com.opengamma.financial.analytics.model.volatility.surface.black.BlackVolatilitySurfacePropertyNamesAndValues;
import com.opengamma.financial.property.DefaultPropertyFunction;
import com.opengamma.util.ArgumentChecker;

/**
 *
 */
public abstract class BlackVolatilitySurfaceDefaults extends DefaultPropertyFunction {
  private static final String[] VALUE_REQUIREMENTS = new String[] {
    ValueRequirementNames.BLACK_VOLATILITY_SURFACE_INTERPOLATOR,
    ValueRequirementNames.BLACK_VOLATILITY_SURFACE,
    ValueRequirementNames.LOCAL_VOLATILITY_SURFACE,
    ValueRequirementNames.PURE_VOLATILITY_SURFACE,
    ValueRequirementNames.FORWARD_DELTA,
    ValueRequirementNames.DUAL_DELTA,
    ValueRequirementNames.DUAL_GAMMA,
    ValueRequirementNames.FORWARD_GAMMA,
    ValueRequirementNames.FOREX_DOMESTIC_PRICE,
    ValueRequirementNames.FOREX_PV_QUOTES,
    ValueRequirementNames.FORWARD_VEGA,
    ValueRequirementNames.FORWARD_VOMMA,
    ValueRequirementNames.FORWARD_VANNA,
    ValueRequirementNames.PRESENT_VALUE,
    ValueRequirementNames.FX_PRESENT_VALUE,
    ValueRequirementNames.IMPLIED_VOLATILITY,
    ValueRequirementNames.GRID_DUAL_DELTA,
    ValueRequirementNames.GRID_DUAL_GAMMA,
    ValueRequirementNames.GRID_FORWARD_DELTA,
    ValueRequirementNames.GRID_FORWARD_GAMMA,
    ValueRequirementNames.GRID_FORWARD_VEGA,
    ValueRequirementNames.GRID_FORWARD_VANNA,
    ValueRequirementNames.GRID_FORWARD_VOMMA,
    ValueRequirementNames.GRID_IMPLIED_VOLATILITY,
    ValueRequirementNames.GRID_PRESENT_VALUE
  };
  private final String _timeAxis;
  private final String _yAxis;
  private final String _volatilityTransform;
  private final String _timeInterpolator;
  private final String _timeLeftExtrapolator;
  private final String _timeRightExtrapolator;

  public BlackVolatilitySurfaceDefaults(final String timeAxis, final String yAxis, final String volatilityTransform, final String timeInterpolator,
      final String timeLeftExtrapolator, final String timeRightExtrapolator) {
    super(ComputationTargetType.LEGACY_PRIMITIVE, true); // // [PLAT-2286]: change to correct type
    ArgumentChecker.notNull(timeAxis, "time axis");
    ArgumentChecker.notNull(yAxis, "y axis");
    ArgumentChecker.notNull(volatilityTransform, "volatility transform");
    ArgumentChecker.notNull(timeInterpolator, "time interpolator");
    ArgumentChecker.notNull(timeLeftExtrapolator, "time left extrapolator");
    ArgumentChecker.notNull(timeRightExtrapolator, "time right extrapolator");
    _timeAxis = timeAxis;
    _yAxis = yAxis;
    _volatilityTransform = volatilityTransform;
    _timeInterpolator = timeInterpolator;
    _timeLeftExtrapolator = timeLeftExtrapolator;
    _timeRightExtrapolator = timeRightExtrapolator;
  }

  @Override
  protected void getDefaults(final PropertyDefaults defaults) {
    for (final String valueRequirement : VALUE_REQUIREMENTS) {
      defaults.addValuePropertyName(valueRequirement, BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_AXIS);
      defaults.addValuePropertyName(valueRequirement, BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_Y_AXIS);
      defaults.addValuePropertyName(valueRequirement, BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_VOLATILITY_TRANSFORM);
      defaults.addValuePropertyName(valueRequirement, BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_INTERPOLATOR);
      defaults.addValuePropertyName(valueRequirement, BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_LEFT_EXTRAPOLATOR);
      defaults.addValuePropertyName(valueRequirement, BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_RIGHT_EXTRAPOLATOR);
    }
  }

  @Override
  protected Set<String> getDefaultValue(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue, final String propertyName) {
    if (BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_AXIS.equals(propertyName)) {
      return Collections.singleton(_timeAxis);
    }
    if (BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_Y_AXIS.equals(propertyName)) {
      return Collections.singleton(_yAxis);
    }
    if (BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_VOLATILITY_TRANSFORM.equals(propertyName)) {
      return Collections.singleton(_volatilityTransform);
    }
    if (BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_INTERPOLATOR.equals(propertyName)) {
      return Collections.singleton(_timeInterpolator);
    }
    if (BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_LEFT_EXTRAPOLATOR.equals(propertyName)) {
      return Collections.singleton(_timeLeftExtrapolator);
    }
    if (BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_TIME_RIGHT_EXTRAPOLATOR.equals(propertyName)) {
      return Collections.singleton(_timeRightExtrapolator);
    }
    return null;
  }

  @Override
  public String getMutualExclusionGroup() {
    return OpenGammaFunctionExclusions.BLACK_VOLATILITY_SURFACE_INTERPOLATOR_DEFAULTS;
  }

  protected static String[] getValueRequirements() {
    return VALUE_REQUIREMENTS;
  }
}
