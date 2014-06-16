package org.blocklang.parser.ast;

/**
 *
 */
public class Num extends AstNode {

    private final double value;

    public Num(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
