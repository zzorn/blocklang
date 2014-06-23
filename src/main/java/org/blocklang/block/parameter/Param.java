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
    Class getType();

    /**
     * @return current value of the parameter.
     */
    <T> T get();

    /**
     * @param value value to set the parameter to.  Must be of the correct type.
     */
    void set(Object value);

}
