package org.blocklang.nodes;

/**
 * Listens to changes of a node.
 */
public interface NodeListener {

    void onNodeChanged(Node node);

}
