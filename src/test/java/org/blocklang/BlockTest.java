package org.blocklang;

import org.blocklang.block.Block;
import org.junit.Assert;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlocks() throws Exception {

        AddBlock block1 = new AddBlock();
        AddBlock block2 = new AddBlock();

        block1.a.set(2.0);
        block1.b.set(3.0);
        block2.a.set(block1.out);
        block2.b.set(7.0);

        block1.calculateOutputs();

        assertEquals(13.0, (Double) block2.out.get(), 0.0001);
    }

}
