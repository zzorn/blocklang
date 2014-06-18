package org.blocklang.block.parameter;

import org.blocklang.block.parameter.ParamBase;
import org.flowutils.Symbol;

/**
 * Input parameter of a node.
 */
public final class Input extends ParamBase {

    private final Object defaultValue;

    public Input(Symbol identifier, ParamType type, String description, Object defaultValue) {
        super(identifier, type, description);
        this.defaultValue = defaultValue;
        set(defaultValue);
    }

    public <T> T getDefaultValue() {
        return (T) defaultValue;
    }

    public final void set(Output source) {
        setSource(source);
    }

}
