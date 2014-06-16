package org.blocklang.nodes;

import org.flowutils.Check;
import org.flowutils.Symbol;

/**
 *
 */
public abstract class ParamBase implements Param {

    private final Symbol identifier;
    private final String description;
    private final ParamType type;
    private final boolean isNumerical;

    private Object objValue;
    private double numValue;
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
        this.isNumerical = type.isNumerical();
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


    @Override public final <T> T getObj() {
        if (source != null) return source.getObj();
        else return (T) objValue;
    }

    @Override public final double getNum() {
        if (source != null) return source.getNum();
        else return numValue;
    }

    public final void set(Object objValue) {
        if (isNumerical) throw new IllegalArgumentException("The type of the parameter '"+identifier+"' is numerical, can not set it to " + objValue);
        if (!type.isInstance(objValue)) throw new IllegalArgumentException("The value " + objValue + " is not an instance of " + type + ", can not assign it to the parameter " + identifier);

        this.objValue = objValue;
        source = null;
    }

    public final void set(double numValue) {
        if (!isNumerical) throw new IllegalArgumentException("The type of the parameter is not numerical, but " + type);

        this.numValue = numValue;
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
