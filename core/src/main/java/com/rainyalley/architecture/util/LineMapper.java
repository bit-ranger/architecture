package com.rainyalley.architecture.util;

public interface LineMapper<T> {


    T map(String line, int index);

    String aggregate(T row);
}
