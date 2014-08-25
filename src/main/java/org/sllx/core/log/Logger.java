package org.sllx.core.log;

/**
 * Created by sllx on 14-8-18.
 */
public interface Logger {
    void debug(Object message);

    void debug(Object message, Throwable ta);

    void info(Object message);

    void info(Object message, Throwable ta);

    void warn(Object message);

    void warn(Object message, Throwable ta);

    void error(Object message);

    void error(Object message, Throwable ta);

    void fatal(Object message);

    void fatal(Object message, Throwable ta);
}
