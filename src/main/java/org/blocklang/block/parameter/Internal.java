package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Parameter representing an internal state variable for a block.
 * Only readable and writable internally, not accessible from outside the block.
 */
// TODO: Should only contain a defaultValue, not currentValue?
public final class Internal extends ParamBase {

    /**
     * @param name name for the parameter, should be unique within the block the parameter is in.
     * @param type       type of the parameter.
     */
    public Internal(Symbol name, Class type) {
        super(name, type);
    }

    /**
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     */
    public Internal(Symbol name, Class type, Object defaultValue) {
        super(name, type, defaultValue);
    }

    /**
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public Internal(Symbol name, Class type, Object defaultValue, String description) {
        super(name, type, defaultValue, description);
    }

    @Override public String getSubtypeName() {
        return "internal";
    }
}
