package org.blocklang.compiler;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.SimpleCompiler;
import org.flowutils.Check;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.blocklang.compiler.ClassBuilder.SourceLocation.*;
import static org.flowutils.Check.notNull;

/**
 * Used to build up source for a class to be compiled,
 * and to compile the class from the source.
 */
// TODO: Extract interface?
public final class ClassBuilder<T> {

    private static final String GENERATED_CLASS_PACKAGE = "org.blocklang.generated";
    private static final String GENERATED_CLASS_NAME = "GeneratedClass";
    private static final String GENERATED_CLASS_FULL_PATH = GENERATED_CLASS_PACKAGE + "." + GENERATED_CLASS_NAME;

    private final Class<T> implementedClass;
    private final Set<Class> importedClasses = new LinkedHashSet<Class>();

    private final Map<SourceLocation, StringBuilder> sourcePortions = new HashMap<SourceLocation, StringBuilder>();

    private final Method calculationMethod;
    private final String[] calculationMethodParameterNames;

    private String name;

    private String generatedSource;
    private Class<? extends T> generatedClass;

    /**
     * @param implementedClass interface or class that the generated class implements or extends.
     * @param calculationMethodName name of the method that does the main calculation, and that code can be generated into.
     *                              There should not be several methods with this name in the implemented class.
     */
    public ClassBuilder(Class<T> implementedClass, String calculationMethodName, String ... calculationMethodParameterNames) {
        notNull(implementedClass, "implementedClass");
        Check.identifier(calculationMethodName, "calculationMethodName");
        for (String calculationMethodParameterName : calculationMethodParameterNames) {
            Check.identifier(calculationMethodParameterName, "one of the calculation method parameter names");
        }

        this.implementedClass = implementedClass;
        this.calculationMethodParameterNames = calculationMethodParameterNames;

        // Find calculation method to implement
        calculationMethod = getMethod(implementedClass, calculationMethodName);
        if (calculationMethod == null) {
            throw new IllegalArgumentException("No method with the name '"+calculationMethodName+"' " +
                                               "found in the class to be implemented ("+implementedClass.getName()+")");
        }

        if (Modifier.isFinal(calculationMethod.getModifiers())) {
            throw new IllegalArgumentException("The calculation method '" + calculationMethodName + "' " +
                                               "is final in the class to be implemented (" + implementedClass.getName() + ")");
        }

        if (Modifier.isPrivate(calculationMethod.getModifiers())) {
            throw new IllegalArgumentException("The calculation method '" + calculationMethodName + "' " +
                                               "is private in the class to be implemented (" + implementedClass.getName() + ")");
        }

        Check.equal(calculationMethodParameterNames.length, "number of calculation method parameter names provided",
                    calculationMethod.getParameterTypes().length, "Number of calculation method parameters returned by Java reflection");

        // Setup string builders for each location
        for (SourceLocation sourceLocation : SourceLocation.values()) {
            sourcePortions.put(sourceLocation, new StringBuilder());
        }

        // Add imports for the class/interface to extend/implement, and the return and parameter types of the calculation method to implement.
        importType(implementedClass);
        importType(calculationMethod.getReturnType());
        for (Class<?> parameterType : calculationMethod.getParameterTypes()) {
            importType(parameterType);
        }

    }

    /**
     * @return descriptive name of the generated class.  Not actually used as the class name, but used in error messages etc.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name descriptive name of the generated class.  Not actually used as the class name, but used in error messages etc.
     */
    public void setName(String name) {
        Check.nonEmptyString(name, "name");
        this.name = name;
    }

    /**
     * Imports a specified class.
     */
    public void importType(Class type) {
        notNull(type, "type");

        if (type.isArray()) {
            // Import the component type of the array
            importType(type.getComponentType());
        }
        else if (!type.isPrimitive()) {
            // Add import statement, if not already added
            if (!importedClasses.contains(type)) {
                importedClasses.add(type);

                addSourceLine(IMPORTS, "import " + type.getCanonicalName() + ";");
            }
        }
    }


    /**
     * Adds the specified line to the source at the specified location.
     * Indentation and terminating newline will be added to the line.
     */
    public void addSourceLine(SourceLocation location, String line) {
        notNull(location, "location");
        notNull(line, "line");

        sourcePortions.get(location).
                append(location.getIndent()).
                append(line).
                append("\n");

        codeChanged();
    }


    /**
     * Constructs the source from the currently added code parts if needed, and returns it.
     */
    public String createSource() {

        if (generatedSource == null) {
            // Support both extending a base class or implementing an interface
            String implementsOrExtends = implementedClass.isInterface() ? "implements" : "extends";

            // Construct code for parameter section of calculation method definition
            StringBuilder calculationMethodParameters = new StringBuilder();
            Class<?>[] parameterTypes = calculationMethod.getParameterTypes();
            calculationMethodParameters.append("(");
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) calculationMethodParameters.append(", ");

                Class<?> parameterType = parameterTypes[i];
                String parameterName = calculationMethodParameterNames[i];

                calculationMethodParameters.append(parameterType.getCanonicalName());
                calculationMethodParameters.append(" ");
                calculationMethodParameters.append(parameterName);
            }
            calculationMethodParameters.append(")");

            // Drop in supplied source into a skeleton for the generated class
            generatedSource =
                    "\n" +
                    "// Source generated with ClassBuilder: \n" +
                    "package " +
                    GENERATED_CLASS_PACKAGE +
                    ";\n" +
                    sourcesFor(IMPORTS) +
                    "public final class " +
                    GENERATED_CLASS_NAME +
                    " " +
                    implementsOrExtends +
                    " " +
                    implementedClass.getName() +
                    " {\n" +
                    "  \n" +
                    sourcesFor(FIELDS) +
                    "  \n" +
                    "  // Calculation method\n" +
                    "  public " +
                    calculationMethod.getReturnType().getCanonicalName() +
                    " " +
                    calculationMethod.getName() +
                    calculationMethodParameters.toString() +
                    " {\n" +
                    sourcesFor(BEFORE_CALCULATION) +
                    sourcesFor(AT_CALCULATION) +
                    sourcesFor(AFTER_CALCULATION) +
                    "  }\n" +
                    "  \n" +
                    sourcesFor(METHODS) +
                    "}\n\n";

        }

        return generatedSource;
    }

    /**
     * Compiles the current code if it has changed since the last call, and returns the compiled class.
     *
     * @return class representing the compiled code.
     * @throws CompilationException if there was any problem compiling the code.
     */
    public Class<? extends T> createClass() throws CompilationException {
        if (generatedClass == null) {
            // Construct source
            final String generatedSource = createSource();

            // Compile it
            try {
                // Compile
                SimpleCompiler janinoCompiler = new SimpleCompiler();
                janinoCompiler.cook(name, generatedSource);

                // Get compiled class
                generatedClass = (Class<? extends T>) janinoCompiler.getClassLoader().loadClass(GENERATED_CLASS_FULL_PATH);
            } catch (CompileException e) {
                throw new CompilationException(e, name,
                                               generatedSource, "There was a compile error in the generated source code.");
            } catch (ClassNotFoundException e) {
                throw new CompilationException(e, name, null, "Could not compile the generated code because a requested class was not found.");
            }
        }

        return generatedClass;
    }

    /**
     * Compiles the current code if it has changed since the last call,
     * and returns an instance of the compiled class, using a no-arguments constructor.
     *
     * @return new instance of the generated class.
     * @throws CompilationException if there was any problem compiling the code or creating the instance.
     */
    public T createInstance() throws CompilationException {

        // Compile the generated class
        Class<? extends T> generatedClass = createClass();

        try {
            // Create a new instance of it by calling the constructor
            return generatedClass.newInstance();

        } catch (InstantiationException e) {
            throw new CompilationException(e, name, null, "Could not instantiate the compiled class.");
        } catch (IllegalAccessException e) {
            throw new CompilationException(e, name, null, "Could not access the compiled code.");
        }
    }


    /**
     * Should be called when anything is added, removed, or modified in the code.
     * Ensures the generated class is recompiled next time it is requested.
     */
    private void codeChanged() {
        generatedSource = null;
        generatedClass = null;
    }

    /**
     * @return all sources added to the specified location, as a string, in the order they were added.
     */
    private String sourcesFor(final SourceLocation location) {
        final String locationSources = sourcePortions.get(location).toString();

        if (locationSources.isEmpty()) {
            return "";
        }
        else {
            return "\n" +
                   location.getIndent() + "// " + location.toString() + "\n" +
                   locationSources +
                   "\n";
        }
    }

    private Method getMethod(Class<T> classToSearch, String methodName) {
        for (Method method : classToSearch.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * Indicates where in the generated source some code should be placed.
     * Used when adding source to SourceBuilder.
     */
    public static enum SourceLocation {
        IMPORTS(0, false, false),
        FIELDS(1, true, false),
        METHODS(1, false, false),
        BEFORE_CALCULATION(2, true, true),
        AT_CALCULATION(2, true, true),
        AFTER_CALCULATION(2, true, true)
        ;

        private String indent = "";
        private final boolean validVariableLocation;
        private final boolean validAssignmentLocation;

        private SourceLocation(int indentSize, boolean validVariableLocation, boolean validAssignmentLocation) {
            this.validVariableLocation = validVariableLocation;
            this.validAssignmentLocation = validAssignmentLocation;

            for (int i = 0; i < indentSize; i++) {
                indent += "  ";
            }
        }

        public String getIndent() {
            return indent;
        }

        public boolean isValidVariableLocation() {
            return validVariableLocation;
        }

        public boolean isValidAssignmentLocation() {
            return validAssignmentLocation;
        }
    }
}
