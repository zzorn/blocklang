package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 *
 */
// TODO: Add listener support
public interface Param {

    /**
     * @return identifier of the parameter.
     */
    Symbol getIdentifier();

    /**
     * @return user readable description of the parameter, or null if none available.
     */
    String getDescription();

    /**
     * @return type of the parameter.
     */
    ParamType getType();

    /**
     * @return current value of the parameter, if it is of an object type.
     */
    <T> T get();

}
