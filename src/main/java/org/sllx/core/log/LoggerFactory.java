package org.sllx.core.log;

/**
 * Created by sllx on 14-8-18.
 */
public class LoggerFactory {
    public static Logger getLogger(Class clazz){
        return new Log4jProxy(clazz);
    }
}
