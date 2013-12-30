package org.blocklang;

/**
 * A set of compiled classes.
 */
public interface Compilation {

    <T> Class<T> getClass(String className, Class<T> expectedType);

    <T> T instantiateClass(String className, Class<T> expectedType, Object ... parameters);

}
