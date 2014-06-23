package org.blocklang.block.parameter;

import org.blocklang.block.parameter.ParamBase;
import org.flowutils.Symbol;

/**
 * Input parameter of a node.
 */
public final class Input extends SourceableParamBase {

    public Input(Symbol identifier, Class type) {
        super(identifier, type);
    }

    public Input(Symbol identifier, Class type, Object defaultValue) {
        super(identifier, type, defaultValue);
    }

    public Input(Symbol identifier, Class type, Object defaultValue, String description) {
        super(identifier, type, defaultValue, description);
    }
}
