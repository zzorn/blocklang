package org.blocklang.compiler;

import org.flowutils.collections.props.Props;
import org.flowutils.collections.props.ReadableProps;
import org.flowutils.collections.props.WritableProps;

/**
 * Represents the functionality of a block.  Interface used by generated compiled code.
 */
public interface BlockCalculation {

    /**
     * @param inputParameters input parameters to the calculation.
     * @param internalParameters internal state parameters in the calculation.
     *                           Can be used to keep block state in over subsequent calculations,
     *                           in applications where that makes sense (e.g. simulating systems, or generating sound, etc).
     * @param outputParameters output parameters to writhe the results of the calculation to.
     * @param calculationListener optional listener that is notified about the calculation progress and can be used to stop it.
     *                         May not be called at all if the calculation is short.
     *                         Pass in null if no progress listening is desired.
     */
    void calculate(ReadableProps inputParameters,
                   Props internalParameters,
                   WritableProps outputParameters,
                   CalculationListener calculationListener);

}
