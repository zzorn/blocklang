package org.blocklang.block.parameter;

import org.blocklang.block.BlockBuilder;
import org.flowutils.Symbol;

/**
 *
 */
// TODO: Add listener support
public interface Param {

    /**
     * @return name of the parameter.
     */
    Symbol getName();

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

    /**
     * @return string describing the subtype of the parameter (input, output, etc).
     */
    String getSubtypeName();

    /**
     * @return unique identifier to use for this parameter in the code generated with the specified blockBuilder.
     */
    String getId(BlockBuilder blockBuilder);
}
