package org.blocklang.block.parameter;

import org.flowutils.Symbol;

/**
 * Base class for parameters that may have a source parameter specified as the parameter to retrieve the value from.
 */
public abstract class SourceableParamBase extends ParamBase {

    private Param source = null;

    protected SourceableParamBase(Symbol identifier,
                                  Class type) {
        super(identifier, type);
    }

    protected SourceableParamBase(Symbol identifier,
                                  Class type,
                                  Object defaultValue) {
        super(identifier, type, defaultValue);
    }

    protected SourceableParamBase(Symbol identifier,
                                  Class type,
                                  Object defaultValue,
                                  String description) {
        super(identifier, type, defaultValue, description);
    }

    /**
     * @param source source parameter that is used when retrieving the value for a parameter, or null to set to none.
     */
    public final void set(Param source) {
        this.source = source;

        // TODO: Notify source change listeners.
    }

    /**
     * @return the parameter that is used to retrieve the value for this parameter, or null if none.
     */
    public final Param getSource() {
        return source;
    }

    @Override public final <T> T get() {
        if (source != null) return source.get();
        else return super.get();
    }

    @Override public final void set(Object value) {
        super.set(value);

        // Clear current source if we have any
        if (source != null) {
            set((Param)null);
        }
    }
}
