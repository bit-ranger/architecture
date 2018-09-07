package com.rainyalley.architecture.filter.xss;

/**
 * @author bin.zhang
 */
public interface XssChecker {

    boolean isValid(String param);
}
