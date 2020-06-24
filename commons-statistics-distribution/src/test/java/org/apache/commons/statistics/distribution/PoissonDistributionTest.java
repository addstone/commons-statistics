/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.statistics.distribution;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <code>PoissonDistributionTest</code>
 */
public class PoissonDistributionTest extends DiscreteDistributionAbstractTest {

    /**
     * Poisson parameter value for the test distribution.
     */
    private static final double DEFAULT_TEST_POISSON_PARAMETER = 4.0;

    //---------------------- Override tolerance --------------------------------

    @BeforeEach
    public void customSetUp() {
        setTolerance(1e-12);
    }

    //-------------- Implementations for abstract methods ----------------------

    /**
     * Creates the default discrete distribution instance to use in tests.
     */
    @Override
    public DiscreteDistribution makeDistribution() {
        return new PoissonDistribution(DEFAULT_TEST_POISSON_PARAMETER);
    }

    /**
     * Creates the default probability density test input values.
     */
    @Override
    public int[] makeDensityTestPoints() {
        return new int[] {-1, 0, 1, 2, 3, 4, 5, 10, 15, 16, 20, Integer.MAX_VALUE};
    }

    /**
     * Creates the default probability density test expected values.
     * These and all other test values are generated by R, version 1.8.1
     */
    @Override
    public double[] makeDensityTestValues() {
        return new double[] {0d, 0.0183156388887d,  0.073262555555d,
                             0.14652511111d, 0.195366814813d, 0.195366814813d,
                             0.156293451851d, 0.00529247667642d, 1.503911676283e-05d,
                             3.759779190708e-06d, 8.27746364655e-09, 0d};
    }

    /**
     * Creates the default logarithmic probability density test expected values.
     * Reference values are from R, version 2.14.1.
     */
    @Override
    public double[] makeLogDensityTestValues() {
        return new double[] {Double.NEGATIVE_INFINITY, -4.000000000000d,
                             -2.613705638880d, -1.920558458320d, -1.632876385868d,
                             -1.632876385868d, -1.856019937183d, -5.241468961877d,
                             -11.1048559670425d, -12.4911503281624d, -18.609729238356d,
                             Double.NEGATIVE_INFINITY};
    }

    /**
     * Creates the default cumulative probability density test input values.
     */
    @Override
    public int[] makeCumulativeTestPoints() {
        return new int[] {-1, 0, 1, 2, 3, 4, 5, 10, 20, Integer.MAX_VALUE};
    }

    /**
     * Creates the default cumulative probability density test expected values.
     */
    @Override
    public double[] makeCumulativeTestValues() {
        return new double[] {0d, 0.0183156388887d, 0.0915781944437d,
                             0.238103305554d, 0.433470120367d, 0.62883693518d,
                             0.78513038703d, 0.99716023388d, 0.999999998077, 1.0};
    }

    /**
     * Creates the default inverse cumulative probability test input values.
     */
    @Override
    public double[] makeInverseCumulativeTestPoints() {
        final DiscreteDistribution dist = getDistribution();
        return new double[] {0d, 0.018315638886d, 0.018315638890d,
                             0.091578194441d, 0.091578194445d, 0.238103305552d,
                             0.238103305556d, dist.cumulativeProbability(3),
                             dist.cumulativeProbability(4), dist.cumulativeProbability(5),
                             dist.cumulativeProbability(10), dist.cumulativeProbability(20)};
    }

    /**
     * Creates the default inverse cumulative probability density test expected values.
     */
    @Override
    public int[] makeInverseCumulativeTestValues() {
        return new int[] {0, 0, 1, 1, 2, 2, 3, 3, 4, 5, 10, 20};
    }

    //-------------------- Additional test cases -------------------------------

    /**
     * Test the normal approximation of the Poisson distribution by
     * calculating P(90 &le; X &le; 110) for X = Po(100) and
     * P(9900 &le; X &le; 10200) for X  = Po(10000)
     */
    @Test
    public void testNormalApproximateProbability() {
        PoissonDistribution dist = new PoissonDistribution(100);
        double result = dist.normalApproximateProbability(110) -
            dist.normalApproximateProbability(89);
        Assertions.assertEquals(0.706281887248, result, 1e-10);

        dist = new PoissonDistribution(10000);
        result = dist.normalApproximateProbability(10200) -
            dist.normalApproximateProbability(9899);
        Assertions.assertEquals(0.820070051552, result, 1E-10);
    }

    /**
     * Test the degenerate cases of a 0.0 and 1.0 inverse cumulative probability.
     */
    @Test
    public void testDegenerateInverseCumulativeProbability() {
        final PoissonDistribution dist = new PoissonDistribution(DEFAULT_TEST_POISSON_PARAMETER);
        Assertions.assertEquals(Integer.MAX_VALUE, dist.inverseCumulativeProbability(1.0d));
        Assertions.assertEquals(0, dist.inverseCumulativeProbability(0d));
    }

    @Test
    public void testParameterAccessors() {
        final PoissonDistribution dist = new PoissonDistribution(10.0);
        Assertions.assertEquals(10.0, dist.getMean(), 0.0);
    }

    @Test
    public void testConstructorPrecondition1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PoissonDistribution(-1));
    }

    @Test
    public void testMoments() {
        final double tol = 1e-9;
        PoissonDistribution dist;

        dist = new PoissonDistribution(1);
        Assertions.assertEquals(1, dist.getMean(), tol);
        Assertions.assertEquals(1, dist.getVariance(), tol);

        dist = new PoissonDistribution(11.23);
        Assertions.assertEquals(11.23, dist.getMean(), tol);
        Assertions.assertEquals(11.23, dist.getVariance(), tol);
    }

    @Test
    public void testLargeMeanCumulativeProbability() {
        double mean = 1.0;
        while (mean <= 10000000.0) {
            final PoissonDistribution dist = new PoissonDistribution(mean);

            double x = mean * 2.0;
            final double dx = x / 10.0;
            double p = Double.NaN;
            final double sigma = Math.sqrt(mean);
            while (x >= 0) {
                try {
                    p = dist.cumulativeProbability((int) x);
                    Assertions.assertFalse(Double.isNaN(p), "NaN cumulative probability");
                    if (x > mean - 2 * sigma) {
                        Assertions.assertTrue(p > 0, "Zero cumulative probaility");
                    }
                } catch (final AssertionError ex) {
                    Assertions.fail("mean of " + mean + " and x of " + x + " caused " + ex.getMessage());
                }
                x -= dx;
            }

            mean *= 10.0;
        }
    }

    /**
     * JIRA: MATH-282
     */
    @Test
    public void testCumulativeProbabilitySpecial() {
        PoissonDistribution dist;
        dist = new PoissonDistribution(9120);
        checkProbability(dist, 9075);
        checkProbability(dist, 9102);
        dist = new PoissonDistribution(5058);
        checkProbability(dist, 5044);
        dist = new PoissonDistribution(6986);
        checkProbability(dist, 6950);
    }

    private void checkProbability(PoissonDistribution dist, int x) {
        final double p = dist.cumulativeProbability(x);
        Assertions.assertFalse(Double.isNaN(p), () -> "NaN cumulative probability returned for mean = " +
                dist.getMean() + " x = " + x);
        Assertions.assertTrue(p > 0, () -> "Zero cum probability returned for mean = " +
                dist.getMean() + " x = " + x);
    }

    @Test
    public void testLargeMeanInverseCumulativeProbability() {
        double mean = 1.0;
        while (mean <= 100000.0) { // Extended test value: 1E7.  Reduced to limit run time.
            final PoissonDistribution dist = new PoissonDistribution(mean);
            double p = 0.1;
            final double dp = p;
            while (p < .99) {
                try {
                    final int ret = dist.inverseCumulativeProbability(p);
                    // Verify that returned value satisties definition
                    Assertions.assertTrue(p <= dist.cumulativeProbability(ret));
                    Assertions.assertTrue(p > dist.cumulativeProbability(ret - 1));
                } catch (final AssertionError ex) {
                    Assertions.fail("mean of " + mean + " and p of " + p + " caused " + ex.getMessage());
                }
                p += dp;
            }
            mean *= 10.0;
        }
    }
}
