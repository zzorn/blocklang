package org.blocklang.compiler;

/**
 * Listener that is notified about a calculation progress, and can be used to stop it.
 */
public interface ProgressListener {

    /**
     * @param progress progress of the calculation, goes from zero to one.
     * @return true to continue, false to stop calculation.
     */
    boolean onProgress(double progress);

}
