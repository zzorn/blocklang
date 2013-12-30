package org.blocklang;

/**
 *
 */
public interface LanguageParser {

    Module parseModule(String source);

    Function parseFunction(String source);

}
