package com.rainyalley.architecture.event;

import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

/**
 * @author bin.zhang
 */
public class Emitter<T> implements Consumer<T> {

    private FluxSink<T> sink;

    public void setSink(FluxSink<T> sink) {
        this.sink = sink;
    }

    @Override
    public void accept(T event) {
        if(sink == null){
            return;
        }
        sink.next(event);
    }
}