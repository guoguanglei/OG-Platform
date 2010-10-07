/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.view;

import java.util.Collection;
import java.util.Map;

import com.opengamma.engine.value.ComputedValue;
import com.opengamma.util.PublicAPI;

/**
 * The result of a single target across all configurations of a view calculation. 
 */
@PublicAPI
public interface ViewTargetResultModel {

  Collection<String> getCalculationConfigurationNames();

  Map<String, ComputedValue> getValues(String calcConfigurationName);

}
