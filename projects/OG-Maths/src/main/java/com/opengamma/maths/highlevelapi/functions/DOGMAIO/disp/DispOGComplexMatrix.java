/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAIO.disp;

import com.opengamma.maths.dogma.engine.DOGMAMethodHook;
import com.opengamma.maths.dogma.engine.methodhookinstances.unaryvoid.Disp;
import com.opengamma.maths.highlevelapi.datatypes.OGComplexMatrix;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;

/**
 * Displays comlpex matrices
 */
@DOGMAMethodHook(provides = Disp.class)
public class DispOGComplexMatrix implements Disp<OGComplexMatrix> {

  @Override
  public void eval(OGComplexMatrix array1) {
    Catchers.catchNull(array1);
    double[] data = array1.getData();
    int rows = array1.getNumberOfRows();
    int columns = array1.getNumberOfColumns();
    String str = "\nOGComplexMatrix:\n";
    double imag;
    for (int i = 0; i < 2 * rows; i += 2) {
      for (int j = 0; j < columns - 1; j++) {
        imag = data[j * 2 * rows + i + 1];
        str += String.format("%24.18f " + (imag >= 0 ? "    +" : "    -") + "%24.18fi, ", data[j * 2 * rows + i], Math.abs(imag));
      }
      imag = data[(columns - 1) * 2 * rows + i + 1];
      str += String.format("%24.18f " + (imag >= 0 ? "    +" : "    -") + "%24.18fi, ", data[(columns - 1) * 2 * rows + i], Math.abs(imag));
      str += String.format("\n");
    }
    System.out.println(str);
  }

}