package org.blocklang.compiler;

import org.codehaus.commons.compiler.LocatedException;

/**
 * Exception thrown by RendererBuilder in case of errors.
 */
public class CompilationException extends Exception {

    private final String sourceName;
    private final String source;

    public CompilationException(Throwable cause,
                                String sourceName,
                                String source,
                                String message) {
        super(message +
              "\nIn the generated source for '"+sourceName+"'" +
              (cause instanceof LocatedException ? "\nThe location in the source is " + ((LocatedException)cause).getLocation().toString() : "" ) +
              "\nThe exception was: " + cause.getMessage() +
              (source != null ? "\nThe full generated source was: \n\n" + source : ""),
              cause);
        this.sourceName = sourceName;
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
