package org.blocklang.parser;

import org.blocklang.parser.Compilation;
import org.blocklang.parser.LanguageParser;

/**
 *
 */
public class JaninoEnvironment extends EnvironmentBase {

    public JaninoEnvironment() {
    }

    public JaninoEnvironment(final String packageBase) {
        super(packageBase);
    }

    public JaninoEnvironment(final LanguageParser parser, final String packageBase) {
        super(parser, packageBase);
    }

    @Override
    public Compilation compile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
