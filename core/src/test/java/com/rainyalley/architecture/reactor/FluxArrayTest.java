package com.rainyalley.architecture.reactor;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class FluxArrayTest {


    @Test
    public void subscribe() {
        ArchFlux.just(1,2,3,4,5).subscribe(
                new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        s.request(9999);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                }
        );

    }
}