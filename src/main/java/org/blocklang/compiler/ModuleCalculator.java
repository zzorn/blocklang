package org.blocklang.compiler;

import org.flowutils.collections.props.Props;
import org.flowutils.collections.props.ReadableProps;
import org.flowutils.collections.props.WritableProps;

import java.util.Map;

/**
 * Represents the functionality of a block or composite block.  Interface used by generated compiled code.
 */
public interface ModuleCalculator {

    /**
     * @param externalContext global context parameters for the calculation (e.g. current time).
     * @param inputParameters input parameters to the calculation.
     * @param outputParameters output parameters to writhe the results of the calculation to.
     * @param calculationListener optional listener that is notified about the calculation progress and can be used to stop it.
     *                         May not be called at all if the calculation is short.
     *                         Pass in null if no progress listening is desired.
     */
    void update(ReadableProps externalContext,
                ReadableProps inputParameters,
                WritableProps outputParameters,
                CalculationListener calculationListener);

    void initializeDefaults(Map<String, Object> parameterDefaults);


    /* TODO: Add support for serialization - maybe rework how and where parameter values are stored in general.
    /**
     * Retrieve the values of any internal parameters
     * Can be used to serialize the state of the calculator.
     *
     * @param internalParameters a WritableProps instance that the internal parameters will be written to (it is not cleared in advance).
     * /
    void getInternalParameters(WritableProps internalParameters);

    /**
     * Set the values of any internal parameters.
     * Can be used to deserialize the state of the calculator.
     *
     * @param internalParameters internal state parameters in the calculation.
     *                           Can be used to keep block state in over subsequent calculations,
     *                           in applications where that makes sense (e.g. simulating systems, or generating sound, etc).
     * /
    void setInternalParameters(ReadableProps internalParameters);
    */

}
