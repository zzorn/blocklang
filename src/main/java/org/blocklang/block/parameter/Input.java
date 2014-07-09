package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Input parameter of a node.
 */
public final class Input extends SourceableParamBase {

    public Input(Symbol name, Class type) {
        super(name, type);
    }

    public Input(Symbol name, Class type, Object defaultValue) {
        super(name, type, defaultValue);
    }

    public Input(Symbol name, Class type, Object defaultValue, String description) {
        super(name, type, defaultValue, description);
    }

    @Override public String getSubtypeName() {
        return "input";
    }
}
