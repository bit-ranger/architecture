package org.sllx.core.log;


class Log4jProxy implements Logger{

    private org.apache.log4j.Logger log4jLogger;

    Log4jProxy(Class clazz){
        this.log4jLogger = org.apache.log4j.Logger.getLogger(clazz);
    }

    @Override
    public void debug(Object message){
        log4jLogger.debug(message);
    }

    @Override
    public void debug(Object message, Throwable ta){
        log4jLogger.debug(message, ta);
    }

    @Override
    public void info(Object message){
        log4jLogger.info(message);
    }

    @Override
    public void info(Object message, Throwable ta){
        log4jLogger.info(message, ta);
    }

    @Override
    public void warn(Object message){
        log4jLogger.warn(message);
    }

    @Override
    public void warn(Object message, Throwable ta){
        log4jLogger.warn(message, ta);
    }

    @Override
    public void error(Object message){
        log4jLogger.error(message);
    }

    @Override
    public void error(Object message, Throwable ta){
        log4jLogger.error(message, ta);
    }

    @Override
    public void fatal(Object message){
        log4jLogger.fatal(message);
    }

    @Override
    public void fatal(Object message, Throwable ta){
        log4jLogger.fatal(message, ta);
    }
}
