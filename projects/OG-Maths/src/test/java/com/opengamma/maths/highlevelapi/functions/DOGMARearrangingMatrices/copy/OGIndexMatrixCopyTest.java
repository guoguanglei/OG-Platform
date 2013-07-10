/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMARearrangingMatrices.copy;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.opengamma.maths.highlevelapi.datatypes.OGIndexMatrix;
import com.opengamma.maths.highlevelapi.functions.DOGMARearrangingMatrices.copy.CopyOGIndexMatrix;

/**
 * test copy OGIndexMatrix
 */
public class OGIndexMatrixCopyTest {

  @Test
  public void copyTest() {
    OGIndexMatrix foo = new OGIndexMatrix(new int[] {1, 2, 3, 4 }, 2, 2);
    CopyOGIndexMatrix copy = new CopyOGIndexMatrix();
    assertTrue(foo.fuzzyequals(copy.eval(foo), 1e-14));

  }

}