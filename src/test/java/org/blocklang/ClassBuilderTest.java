package org.blocklang;

import org.blocklang.compiler.ClassBuilder;
import org.junit.Assert;

import static org.blocklang.compiler.ClassBuilder.SourceLocation.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ClassBuilderTest {

    @Test
    public void testClassBuilder() throws Exception {

        ClassBuilder<TestCalculation1> builder = new ClassBuilder<TestCalculation1>(TestCalculation1.class, "calculateSomeStuff", "fooString", "numbers");
        builder.addSourceLine(BEFORE_CALCULATION, "int sum = 0;");
        builder.addSourceLine(AT_CALCULATION, "for (int i = 0; i < numbers.length; i++) {");
        builder.addSourceLine(AT_CALCULATION, "  sum += numbers[i];");
        builder.addSourceLine(AT_CALCULATION, "}");
        builder.addSourceLine(AFTER_CALCULATION, "return sum;");

        final TestCalculation1 calculator = builder.createInstance();

        Assert.assertEquals(2+4+8, calculator.calculateSomeStuff("bar", 2, 4, 8));
    }


    public static interface TestCalculation1 {
        int calculateSomeStuff(String aString, int ... numbers);
    }
}
