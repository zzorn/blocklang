package org.blocklang.nodes;

import org.flowutils.Symbol;

import java.util.Map;

/**
 *
 */
public interface Context {

    /**
     * @return current state of the context.
     */
    Map<Symbol, Output> getState();

}
