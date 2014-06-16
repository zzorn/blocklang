package org.blocklang;

import org.blocklang.parser.Compilation;
import org.blocklang.parser.JaninoEnvironment;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class CompileTest {

    private Environment environment;

    @Before
    public void setUp() throws Exception {
        environment = new JaninoEnvironment();
    }

    @Test public void testCompilation() throws Exception {
        environment.addModule(new File("src/test/blocklang/testmodule.block"));

        final Compilation compilation = environment.compile();

        final Object testModule = compilation.instantiateClass("org.blocklang.usergenerated.TestModule", Object.class, 42, "Foo");

        assertNotNull(testModule);
    }
}
