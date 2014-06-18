package org.blocklang.parser;

import org.blocklang.Function;
import org.blocklang.nodes.Node;

/**
 *
 */
public interface LanguageParser {

    Node parseNode(String source);

}
