/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic;

import java.util.HashMap;
import java.util.Map;

import com.opengamma.maths.commonapi.exceptions.MathsExceptionNotImplemented;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArraySuper;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGDoubleArray;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGSparseArray;
import com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.times.TimesAbstract;
import com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.times.TimesOGDoubleArrayOGDoubleArray;
import com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.times.TimesOGDoubleArrayOGSparseArray;
import com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.times.TimesOGSparseArrayOGSparseArray;
import com.opengamma.util.tuple.ObjectsPair;
import com.opengamma.util.tuple.Pair;

/**
 * Element wise multiplication
 */
public class Times {
  /**
   * hashmapped function pointers
   */
  private static Map<Pair<?, ?>, TimesAbstract<?, ?>> s_functionPointers = new HashMap<Pair<?, ?>, TimesAbstract<?, ?>>();
  static {
    ObjectsPair<Class<?>, Class<?>> pairOGDoubleArrayOGDoubleArray = new ObjectsPair<Class<?>, Class<?>>(OGDoubleArray.class, OGDoubleArray.class);
    ObjectsPair<Class<?>, Class<?>> pairOGDoubleArrayOGSparseArray = new ObjectsPair<Class<?>, Class<?>>(OGDoubleArray.class, OGSparseArray.class);
    ObjectsPair<Class<?>, Class<?>> pairOGSparseArrayOGDoubleArray = new ObjectsPair<Class<?>, Class<?>>(OGSparseArray.class, OGDoubleArray.class);
    ObjectsPair<Class<?>, Class<?>> pairOGSparseArrayOGSparseArray = new ObjectsPair<Class<?>, Class<?>>(OGSparseArray.class, OGSparseArray.class);    
    s_functionPointers.put(pairOGDoubleArrayOGDoubleArray, TimesOGDoubleArrayOGDoubleArray.getInstance());
    s_functionPointers.put(pairOGDoubleArrayOGSparseArray, TimesOGDoubleArrayOGSparseArray.getInstance());
    s_functionPointers.put(pairOGSparseArrayOGSparseArray, TimesOGSparseArrayOGSparseArray.getInstance());        
  }

  @SuppressWarnings("unchecked")
  public <T extends OGArraySuper<Number>, S extends OGArraySuper<Number>> OGArraySuper<Number> times(T array1, S array2) {
    ObjectsPair<Class<?>, Class<?>> combo = new ObjectsPair<Class<?>, Class<?>>(array1.getClass(), array2.getClass());
    TimesAbstract<T, S> use = (TimesAbstract<T, S>) s_functionPointers.get(combo);
    if (use == null) {
      throw new MathsExceptionNotImplemented("Multiplying array class " + array1.getClass().toString() + " and " + array2.getClass().toString() + " is not yet implemented");
    }
    return use.times(array1, array2);
  }

}
