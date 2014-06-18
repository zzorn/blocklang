package org.blocklang.compiler;

import org.flowutils.collections.props.ReadableProps;
import org.flowutils.collections.props.WritableProps;

/**
 * Represents the functionality of a block.  Interface used by generated compiled code.
 */
public interface BlockCalculation {

    /**
     * @param inputParameters input parameters to the calculation.
     * @param outputParameters output parameters to writhe the results of the calculation to.
     * @param calculationListener optional listener that is notified about the calculation progress and can be used to stop it.
     *                         May not be called at all if the calculation is short.
     *                         Pass in null if no progress listening is desired.
     */
    void calculate(ReadableProps inputParameters,
                   WritableProps outputParameters,
                   CalculationListener calculationListener);

}
