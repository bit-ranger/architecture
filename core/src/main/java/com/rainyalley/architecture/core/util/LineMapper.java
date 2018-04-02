package com.rainyalley.architecture.core.util;

public interface LineMapper<T> {


    T map(String line, int index);

    String aggregate(T row);
}
