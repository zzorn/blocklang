package org.blocklang.block.parameter;

import org.flowutils.Check;
import org.flowutils.Symbol;

/**
 * Common functionality for parameters.
 */
public abstract class ParamBase implements Param {

    private final Symbol identifier;
    private final String description;
    private final Class type;
    private final Object defaultValue;

    private Object value;


    /**
     * @param identifier name for the parameter, should be unique within the block the parameter is in.
     * @param type type of the parameter.
     */
    public ParamBase(Symbol identifier, Class type) {
        this(identifier, type, null, null);
    }

    /**
     * @param identifier name for the parameter, should be unique within the block the parameter is in.
     * @param type type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     */
    public ParamBase(Symbol identifier, Class type, Object defaultValue) {
        this(identifier, type, defaultValue, null);
    }

    /**
     * @param identifier name for the parameter, should be unique within the block the parameter is in.
     * @param type type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description human readable description of the parameter.
     */
    public ParamBase(Symbol identifier, Class type, Object defaultValue, String description) {
        Check.notNull(identifier, "identifier");
        Check.notNull(type, "type");

        this.identifier = identifier;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = description;

        set(defaultValue);
    }

    public final Symbol getIdentifier() {
        return identifier;
    }

    public final String getDescription() {
        return description;
    }

    public final Class getType() {
        return type;
    }

    /**
     * @return default value to use for the parameter when uninitialized.
     */
    public final Object getDefaultValue() {
        return defaultValue;
    }

    @Override public <T> T get() {
        return (T) value;
    }

    @Override public void set(Object value) {
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("The value '" + value + "' " +
                                               "of type "+ (value == null ? "null" : value.getClass().getName())+" " +
                                               "is not an instance of " + getType() + ", " +
                                               "can not assign it to the parameter " + getIdentifier());
        }

        this.value = value;
    }

    /**
     * Resets the value of this parameter to its default / initial value.
     */
    public final void resetToDefault() {
        set(getDefaultValue());
    }
}
