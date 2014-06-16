package org.blocklang.parser;

import org.blocklang.Function;
import org.blocklang.Module;

/**
 *
 */
public interface LanguageParser {

    Module parseModule(String source);

    Function parseFunction(String source);

}
