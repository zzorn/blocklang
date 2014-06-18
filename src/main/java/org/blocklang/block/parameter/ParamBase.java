package org.blocklang.block.parameter;

import org.blocklang.block.parameter.Param;
import org.blocklang.block.parameter.ParamType;
import org.flowutils.Check;
import org.flowutils.Symbol;

/**
 *
 */
// TODO: Drop non-common functionality down to implementations
public abstract class ParamBase implements Param {

    private final Symbol identifier;
    private final String description;
    private final ParamType type;

    private Object value;
    private Param source;


    public ParamBase(Symbol identifier, ParamType type) {
        this(identifier, type, null);
    }

    public ParamBase(Symbol identifier, ParamType type, String description) {
        Check.notNull(identifier, "identifier");
        Check.notNull(type, "type");

        this.identifier = identifier;
        this.type = type;
        this.description = description;
    }

    public final Symbol getIdentifier() {
        return identifier;
    }

    public final ParamType getType() {
        return type;
    }

    public final String getDescription() {
        return description;
    }


    @Override public final <T> T get() {
        if (source != null) return source.get();
        else return (T) value;
    }

    public final void set(Object value) {
        if (!type.isInstance(value)) throw new IllegalArgumentException("The value '" + value + "' is not an instance of " + type + ", can not assign it to the parameter " + identifier);

        this.value = value;
        source = null;
    }

    // Package protected
    final void setSource(Param source) {
        if (source != null && !type.assignableFrom(source.getType())) {
            throw new IllegalArgumentException("Can not assign a source with type "+source.getType()+" to parameter " + identifier + " with type " + type);
        }

        this.source = source;
    }
}
