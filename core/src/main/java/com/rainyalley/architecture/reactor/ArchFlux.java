package com.rainyalley.architecture.reactor;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.function.Function;

/**
 * @author bin.zhang
 */
public abstract class ArchFlux<T> implements Publisher<T> {


    @Override
    public abstract void subscribe(Subscriber<? super T> s);


    public static <T> ArchFlux<T> just(T... data) {
        return new ArchFluxArray<>(data);
    }

    public <V> ArchFlux<V> map(Function<? super T, ? extends V> mapper) {
        return new ArchFluxMap<>(this, mapper);
    }


}
