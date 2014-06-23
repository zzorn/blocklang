package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Parameter representing an internal state variable for a block.
 * Only readable and writable internally, not accessible from outside the block.
 */
public final class Internal extends ParamBase {

    /**
     * @param identifier name for the parameter, should be unique within the block the parameter is in.
     * @param type       type of the parameter.
     */
    public Internal(Symbol identifier, Class type) {
        super(identifier, type);
    }

    /**
     * @param identifier   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     */
    public Internal(Symbol identifier, Class type, Object defaultValue) {
        super(identifier, type, defaultValue);
    }

    /**
     * @param identifier   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public Internal(Symbol identifier, Class type, Object defaultValue, String description) {
        super(identifier, type, defaultValue, description);
    }

}
