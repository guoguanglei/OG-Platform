/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMATrigonometry;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.opengamma.maths.dogma.DOGMA;
import com.opengamma.maths.highlevelapi.datatypes.OGArray;
import com.opengamma.maths.highlevelapi.datatypes.OGComplexMatrix;
import com.opengamma.maths.highlevelapi.datatypes.OGComplexSparseMatrix;
import com.opengamma.maths.highlevelapi.datatypes.OGMatrix;
import com.opengamma.maths.highlevelapi.datatypes.OGSparseMatrix;
import com.opengamma.maths.lowlevelapi.functions.D1mach;
import com.opengamma.util.test.TestGroup;

/**
 * Tests atanh
 */
@Test(groups = TestGroup.UNIT)
public class DOGMA_atanh_Test {

  @Test
  public void testcomplex() {
    double[][] data = new double[][] { {0, 2, 3 }, {0, 5, 0 }, {6, 0, 0 } };
    OGArray<? extends Number> foo, bar, baz;
    foo = new OGComplexSparseMatrix(data, data);
    bar = DOGMA.full(DOGMA.atanh(foo));
    foo = new OGComplexMatrix(data, data);
    baz = DOGMA.full(DOGMA.atanh(foo));
    assertTrue(baz.fuzzyequals(bar, 10*D1mach.four()));
  }

  @Test
  public void testRealNoExpansion() {
    double[][] data = new double[][] { {1, 2, 3 }, {4, 5, 6 }, {7, 8, 9 } };
    OGArray<? extends Number> foo, bar, baz;
    foo = new OGSparseMatrix(data);
    bar = DOGMA.full(DOGMA.atanh(foo));
    foo = new OGMatrix(data);
    baz = DOGMA.full(DOGMA.atanh(foo));
    assertTrue(baz.fuzzyequals(bar, 10*D1mach.four()));
  }
  
}