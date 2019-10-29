package com.rainyalley.architecture;

import com.rainyalley.architecture.event.Emitter;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Repl {



    @Test
    public void repl(){
        Emitter<Integer> emitter = new Emitter<Integer>();
        Publisher<Integer> publisher = Flux.create(emitter::setSink);




        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };

        Flux<Integer> flux = Flux.from(publisher);

        subscribe(flux, subscriber, subscriber);

        Flux.just(1,2,3).doOnNext(emitter).log().publishOn(Schedulers.parallel()).subscribe();

    }

    @SafeVarargs
    private <T> void subscribe(Flux<T> flux, Subscriber<T>... subscriberList) {
        for (Subscriber<T> subscriber : subscriberList) {
            flux = flux.doOnSubscribe(subscriber::onSubscribe)
                    .doOnNext(subscriber::onNext)
                    .doOnError(subscriber::onError)
                    .doOnComplete(subscriber::onComplete);
        }
        flux.subscribe();
    }
}
