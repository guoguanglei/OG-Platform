/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.math.matrix;

import com.opengamma.util.ArgumentChecker;

/**
 * Various utility classes for matrices.
 */
public class DoubleMatrixUtil {

  public static DoubleMatrix2D getIdentityMatrix2D(final int dimension) {
    ArgumentChecker.notNull(dimension, "dimension");
    if (dimension == 0) {
      return DoubleMatrix2D.EMPTY_MATRIX;
    }
    if (dimension == 1) {
      return new DoubleMatrix2D(new double[][] {new double[] {1}});
    }
    final double[][] data = new double[dimension][dimension];
    for (int i = 0; i < dimension; i++) {
      data[i][i] = 1;
    }
    return new DoubleMatrix2D(data);
  }

  public static DoubleMatrix2D getTwoDimensionalDiagonalMatrix(final DoubleMatrix1D vector) {
    ArgumentChecker.notNull(vector, "vector");
    final int n = vector.getNumberOfElements();
    if (n == 0) {
      return DoubleMatrix2D.EMPTY_MATRIX;
    }
    final double[][] data = new double[n][n];
    for (int i = 0; i < n; i++) {
      data[i][i] = vector.getEntry(i);
    }
    return new DoubleMatrix2D(data);
  }
}
