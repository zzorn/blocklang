package org.blocklang.nodes;

/**
 *
 */
public class NumType implements ParamType {

    public static final NumType INSTANCE = new NumType();

    protected NumType() {
    }

    @Override public boolean assignableFrom(ParamType paramType) {
        return NumType.class.isInstance(paramType);
    }

    @Override public boolean isInstance(Object value) {
        return false;
    }

    @Override public boolean isNumerical() {
        return true;
    }
}
