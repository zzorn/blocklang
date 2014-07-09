package org.blocklang;

import org.blocklang.block.BlockBase;
import org.blocklang.block.BlockBuilder;
import org.blocklang.block.parameter.Input;
import org.blocklang.block.parameter.Output;
import org.flowutils.classbuilder.SourceLocation;

import static org.flowutils.classbuilder.SourceLocation.AT_CALCULATION;

/**
 *
 */
public class AddBlock extends BlockBase {

    public final Input a = input("a", 0.0);
    public final Input b = input("b", 0.0);
    public final Output out = output("result");

    @Override protected void doGenerateCode(BlockBuilder blockBuilder) {
        blockBuilder.assign(out, "$a + $b");
    }

}
