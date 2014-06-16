package org.blocklang.nodes;

import org.flowutils.Symbol;

/**
 * Input parameter of a node.
 */
public final class Input extends ParamBase {

    private final Object defaultValueObj;
    private final double defaultValueNum;

    protected Input(Symbol identifier, ParamType type, String description, Object defaultValueObj) {
        super(identifier, type, description);
        this.defaultValueObj = defaultValueObj;
        this.defaultValueNum = Double.NaN;
        set(defaultValueObj);
    }

    protected Input(Symbol identifier, ParamType type, String description, double defaultValueNum) {
        super(identifier, type, description);
        this.defaultValueNum = defaultValueNum;
        this.defaultValueObj = null;
        set(defaultValueNum);
    }

    public <T> T getDefaultValueObj() {
        return (T) defaultValueObj;
    }

    public double getDefaultValueNum() {
        return defaultValueNum;
    }

    public final void set(Output source) {
        setSource(source);
    }
}
