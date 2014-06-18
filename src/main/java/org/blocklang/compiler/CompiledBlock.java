package org.blocklang.compiler;

import org.flowutils.Symbol;
import org.flowutils.collections.props.ReadableProps;
import org.flowutils.collections.props.WritableProps;

import java.util.Map;

/**
 *
 */
public interface CompiledBlock {

    /**
     * @param inputParameters properties with input parameters to the calculation.
     * @param outputParameters properties to write output parameters of the calculation to.
     * @param progressListener optional listener that is notified about the calculation progress and can be used to stop it.
     *                         May not be called at all if the calculation is short.
     *                         Pass in null if no progress listening is desired.
     */
    void calculate(ReadableProps inputParameters,
                   WritableProps outputParameters,
                   ProgressListener progressListener);

}
