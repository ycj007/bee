package com.ycj.bee.refactor.mono;

import reactor.core.publisher.Mono;

public class SimpleCreateMono {


    public static void main(String[] args) {

       Mono empty =  Mono.empty();
       Mono<Integer> mono2 = Mono.just(1);

    }


}
