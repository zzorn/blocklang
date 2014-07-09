package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Result parameter of a node.
 */
public final class Output extends ParamBase {

    /**
     * @param name name for the parameter, should be unique within the block the parameter is in.
     * @param type       type of the parameter.
     */
    public Output(Symbol name, Class type) {
        super(name, type);
    }

    /**
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     */
    public Output(Symbol name, Class type, Object defaultValue) {
        super(name, type, defaultValue);
    }

    /**
     * @param name   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public Output(Symbol name, Class type, Object defaultValue, String description) {
        super(name, type, defaultValue, description);
    }

    @Override public String getSubtypeName() {
        return "output";
    }
}
