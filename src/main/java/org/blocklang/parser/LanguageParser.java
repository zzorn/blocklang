package org.blocklang.parser;

import org.blocklang.block.Block;

/**
 *
 */
public interface LanguageParser {

    Block parseNode(String source);

}
