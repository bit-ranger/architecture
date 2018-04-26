package com.rainyalley.architecture.notification;

public interface Subscriber {
    void receive(Object topic, Object message);
}
