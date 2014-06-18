package org.blocklang.block.parameter;

/**
 * Describes the type of a parameter.
 */
public interface ParamType {

    public boolean assignableFrom(ParamType paramType);

    public boolean isInstance(Object value);

    /**
     * @return true if the type is numerical (double).
     */
    boolean isNumerical();
}
