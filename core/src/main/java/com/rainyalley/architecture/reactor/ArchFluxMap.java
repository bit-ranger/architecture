package com.rainyalley.architecture.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

public class ArchFluxMap<T, R> extends ArchFlux<R> {

    private final ArchFlux<? extends T> source;
    private final Function<? super T, ? extends R> mapper;


    public ArchFluxMap(ArchFlux<? extends T> source, Function<? super T, ? extends R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> s) {
        source.subscribe(new MapSubscriber<>(s, mapper));
    }

    static final class MapSubscriber<T, R> implements Subscription, Subscriber<T> {

        private final Subscriber<? super R> subscriber;
        private final Function<? super T, ? extends R> mapper;
        private boolean done;
        private Subscription subscriptionOfUpstream;


        public MapSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends R> mapper) {
            this.subscriber = subscriber;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Subscription s) {
            this.subscriptionOfUpstream = s;
            subscriber.onSubscribe(this);
        }

        @Override
        public void request(long n) {
            this.subscriptionOfUpstream.request(n);
        }

        @Override
        public void cancel() {
            this.subscriptionOfUpstream.cancel();
        }


        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            subscriber.onNext(mapper.apply(t));
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                return;
            }
            done = true;
            subscriber.onError(t);
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            done = true;
            subscriber.onComplete();
        }
    }
}
