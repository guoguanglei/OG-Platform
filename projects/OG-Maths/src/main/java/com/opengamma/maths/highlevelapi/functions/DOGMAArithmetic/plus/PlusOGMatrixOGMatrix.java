/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAArithmetic.plus;

import com.opengamma.maths.dogma.engine.DOGMAMethodHook;
import com.opengamma.maths.dogma.engine.methodhookinstances.infix.Plus;
import com.opengamma.maths.highlevelapi.datatypes.OGMatrix;
import com.opengamma.maths.lowlevelapi.exposedapi.EasyIZY;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;

/**
 * Adds {@link OGMatrix} to {@link OGMatrix}
 */
@DOGMAMethodHook(provides = Plus.class)
public final class PlusOGMatrixOGMatrix implements Plus<OGMatrix, OGMatrix, OGMatrix> {


  @Override
  public OGMatrix eval(OGMatrix array1, OGMatrix array2) {
    int rowsArray1 = array1.getNumberOfRows();
    int columnsArray1 = array1.getNumberOfColumns();
    int rowsArray2 = array2.getNumberOfRows();
    int columnsArray2 = array2.getNumberOfColumns();
    int retRows = 0, retCols = 0;

    int n = array1.getData().length;
    double[] tmp = new double[n];
    System.arraycopy(array1.getData(), 0, tmp, 0, n);
    // Actually adding arrays
    if (rowsArray1 == 1 && columnsArray1 == 1) { // array 1 is scalar, i.e. X+iY
      n = array2.getData().length;
      tmp = new double[n];
      EasyIZY.vd_addx(array2.getData(), array1.getData()[0], tmp);
      retRows = rowsArray2;
      retCols = columnsArray2;
    } else if (rowsArray2 == 1 && columnsArray2 == 1) {
      n = array1.getData().length;
      tmp = new double[n];
      EasyIZY.vd_addx(array1.getData(), array2.getData()[0], tmp);
      retRows = rowsArray1;
      retCols = columnsArray1;
    } else {
      Catchers.catchBadCommute(rowsArray1, "rows in first array", rowsArray2, "rows in second array");
      Catchers.catchBadCommute(columnsArray1, "columns in first array", columnsArray2, "columns in second array");
      n = array1.getData().length;
      tmp = new double[n];
      EasyIZY.vd_add(array1.getData(), array2.getData(), tmp);
      retRows = rowsArray1;
      retCols = columnsArray1;
    }
    return new OGMatrix(tmp, retRows, retCols);
  }
}