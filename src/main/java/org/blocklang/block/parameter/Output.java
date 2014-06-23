package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Result parameter of a node.
 */
public final class Output extends ParamBase {

    /**
     * @param identifier name for the parameter, should be unique within the block the parameter is in.
     * @param type       type of the parameter.
     */
    public Output(Symbol identifier, Class type) {
        super(identifier, type);
    }

    /**
     * @param identifier   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     */
    public Output(Symbol identifier, Class type, Object defaultValue) {
        super(identifier, type, defaultValue);
    }

    /**
     * @param identifier   name for the parameter, should be unique within the block the parameter is in.
     * @param type         type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description  human readable description of the parameter.
     */
    public Output(Symbol identifier, Class type, Object defaultValue, String description) {
        super(identifier, type, defaultValue, description);
    }
}
