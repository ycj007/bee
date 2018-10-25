package com.ycj.bee;


import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

//@Controller
public class MyHandler {


    public Mono<ServerResponse> listPeople(ServerRequest request) {
        Flux<Integer> people =  Flux.range(1,10);
        return ok().body(people,Integer.class);
    }

}
