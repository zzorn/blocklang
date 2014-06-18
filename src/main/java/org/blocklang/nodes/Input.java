package org.blocklang.nodes;

import org.flowutils.Symbol;

/**
 * Input parameter of a node.
 */
public final class Input extends ParamBase {

    private final Object defaultValue;

    protected Input(Symbol identifier, ParamType type, String description, Object defaultValue) {
        super(identifier, type, description);
        this.defaultValue = defaultValue;
        set(defaultValue);
    }


    public final void set(Output source) {
        setSource(source);
    }

}
