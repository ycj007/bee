package com.ycj.bee.refactor.subscriber;

import com.ycj.bee.refactor.flux.SmplerCreateFlux;
import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;

public class SampleSubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscribed");
                request(10);
        }

        public void hookOnNext(T value) {
                System.out.println("next");
                System.out.println(value);
                request(5);
        }

        public static void main(String[] args) {

                SmplerCreateFlux.getFlux().subscribe(new SampleSubscriber<Integer>());
        }

}