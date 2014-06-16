package org.blocklang.nodes;

import org.flowutils.Symbol;

/**
 * Result parameter of a node.
 */
public final class Output extends ParamBase {

    protected Output(Symbol identifier, ParamType type, String description) {
        super(identifier, type, description);
    }

    protected final void setSource(Input source) {
        super.setSource(source);
    }

}
