package org.blocklang;

import org.blocklang.compiler.SourceBuilder;
import org.blocklang.nodes.Context;
import org.blocklang.nodes.Input;
import org.blocklang.nodes.NodeBase;
import org.blocklang.nodes.Output;

/**
 *
 */
public class AddNode extends NodeBase {

    public final Input a = input("a", 0.0);
    public final Input b = input("b", 0.0);
    public final Output out = output("result");

    @Override public void generateCode(SourceBuilder sourceBuilder) {
        // TODO: Implement

    }

    /*
    @Override public void calculateOutputs(Context context) {
        out.set((Double)a.get() + (Double)b.get());
    }
    */
}
