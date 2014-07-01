package org.blocklang;

import org.blocklang.block.BlockBase;
import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.Output;
import org.flowutils.classbuilder.ClassBuilder;

/**
 *
 */
public class AddBlock extends BlockBase {

    public final Input a = input("a", 0.0);
    public final Input b = input("b", 0.0);
    public final Output out = output("result");

    @Override public void generateCode(ClassBuilder classBuilder) {
        // TODO: Implement

    }

    /*
    @Override public void calculateOutputs(Context context) {
        out.set((Double)a.get() + (Double)b.get());
    }
    */
}
