package org.blocklang.block.parameter;

import org.blocklang.block.Block;
import org.blocklang.block.BlockBuilder;
import org.flowutils.Check;
import org.flowutils.Symbol;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for parameters.
 */
public abstract class ParamBase implements Param {

    private final Block host;
    private final Symbol name;
    private final String description;
    private final Class type;
    private final Object defaultValue;

    private Object value;


    /**
     * @param host the block that the parameter is located in.
     * @param name name for the parameter, should be unique within the block the parameter is in.
     * @param type type of the parameter.
     * @param defaultValue initial and default value for the parameter.
     * @param description human readable description of the parameter.
     */
    public ParamBase(Block host, Symbol name, Class type, Object defaultValue, String description) {
        notNull(host, "host");
        notNull(name, "name");
        notNull(type, "type");

        this.host = host;
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = description;

        set(defaultValue);
    }

    public final Symbol getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final Class getType() {
        return type;
    }

    @Override public final Block getHost() {
        return host;
    }

    @Override public final Class getPrimitiveTypeIfPossible() {
        // Report wrapped primitives as raw primitive types
        if (type.equals(Double.class))  return Double.TYPE;
        if (type.equals(Float.class))   return Float.TYPE;
        if (type.equals(Long.class))    return Long.TYPE;
        if (type.equals(Integer.class)) return Integer.TYPE;
        if (type.equals(Short.class))   return Short.TYPE;
        if (type.equals(Byte.class))    return Byte.TYPE;
        if (type.equals(Boolean.class)) return Boolean.TYPE;

        return type;
    }

    @Override public final Object getDefaultValue() {
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
                                               "can not assign it to the parameter " + getName());
        }

        this.value = value;
    }

    @Override public final String getId(BlockBuilder blockBuilder) {
        return blockBuilder.getParamId(this);
    }

    /**
     * Resets the value of this parameter to its default / initial value.
     */
    public final void resetToDefault() {
        set(getDefaultValue());
    }


}
