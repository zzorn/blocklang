package org.blocklang.block.parameter;

import org.blocklang.block.parameter.ParamBase;
import org.flowutils.Symbol;

/**
 * Result parameter of a node.
 */
public final class Output extends ParamBase {

    public Output(Symbol identifier, ParamType type, String description) {
        super(identifier, type, description);
    }

    protected final void setSource(Input source) {
        super.setSource(source);
    }

}
