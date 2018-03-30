package com.rainyalley.architecture.service.util;

public interface LineMapper<T> {


    T map(String line, int index);

    String aggregate(T row);
}
