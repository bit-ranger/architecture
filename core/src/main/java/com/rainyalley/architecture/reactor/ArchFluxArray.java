package com.rainyalley.architecture.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author bin.zhang
 */
public class ArchFluxArray<T> extends ArchFlux<T> {

    private T[] array;

    ArchFluxArray(T[] array) {
        this.array = array;
    }

    @Override
    public void subscribe(Subscriber<? super T> s) {
        s.onSubscribe(new ArraySubscription<>(s, array));
    }

    private static class ArraySubscription<T> implements Subscription {
        final Subscriber<? super T> actual;
        final T[] array;
        int index;
        boolean canceled;

        private ArraySubscription(Subscriber<? super T> actual, T[] array) {
            this.actual = actual;
            this.array = array;
        }

        @Override
        public void request(long n) {
            if (canceled) {
                return;
            }
            long length = array.length;
            for (int i = 0; i < n && index < length; i++) {
                actual.onNext(array[index++]);
            }
            if (index == length) {
                actual.onComplete();
            }
        }

        @Override
        public void cancel() {
            this.canceled = true;
        }
    }
}
