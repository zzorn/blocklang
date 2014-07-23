package org.blocklang;

import org.blocklang.block.Block;
import org.blocklang.block.Module;
import org.blocklang.block.ModuleImpl;
import org.junit.Assert;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlocks() throws Exception {

        Module module = new ModuleImpl();

        AddBlock block1 = module.addBlock(new AddBlock());
        AddBlock block2 = module.addBlock(new AddBlock());

        block1.a.set(2.0);
        block1.b.set(3.0);
        block2.a.set(block1.out);
        block2.b.set(7.0);

        module.update();

        assertEquals(13.0, (Double) block2.out.get(), 0.0001);
    }

}
