package com.ycj.bee.refactor.flux;

import org.apache.commons.lang3.time.DateFormatUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;

public class SmplerCreateFlux {


    public static Flux<Integer> createFlux() {

        Flux.push(data -> {

        });

        return Flux.create(sink -> {


        });
    }


    public static Flux<Integer> generateFlux() {

        return Flux.generate(() -> 0,
                             (state, sink) -> {
                                 sink.next(3 * state);
                                 if (state == 10) sink.complete();
                                 return state + 1;
                             });

    }


    public static Flux<Integer> getFlux() {

        return Flux.range(1, 10);
    }


    public static void main(String[] args) {

        Flux.interval(Duration.ofSeconds(1))
            .doOnNext(d -> {
                System.out.println("b:"+DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern()));
            })
            .

                    flatMap(data -> {

                        return Mono.delay(Duration.ofSeconds(2), Schedulers.single());
                    })

            .doOnNext(data -> {
                System.out.println("a:" + DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern()));
            })
            .log()
            .subscribe();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }


 /*       Flux<String> flux =
                Flux.<String>error(new IllegalArgumentException())
                        .retryWhen(companion -> companion
                                .doOnNext(s -> System.out.println(s + " at " + LocalTime.now()))
                                .zipWith(Flux.range(1, 4), (error, index) -> {
                                    if (index < 4) return index;
                                    else throw Exceptions.propagate(error);
                                })
                                .flatMap(index -> Mono.delay(Duration.ofMillis(index * 100)))
                                .doOnNext(s -> System.out.println("retried at " + LocalTime.now()))
                        );

        flux.subscribe();*/

/*
        Flux.error(new IllegalArgumentException())
                                       .retryWhen(

                                               (error) -> {

                                                   return error.doOnNext(d -> {

                                                       System.out.println(d);
                                                   })
                                                               .zipWith(Flux.range(0, 4), (e, index) -> {
                                                                   if (index < 4) {
                                                                       return index;
                                                                   } else {
                                                                       throw Exceptions.propagate(e);
                                                                   }

                                                               })
                                                               .flatMap(index -> {
                                                                   return Mono.delay(Duration.ofSeconds(10 * index));
                                                               })
                                                               .doOnNext(s -> System.out.println("retried at " + LocalTime.now()));

                                               }


                                       ).subscribe();*/




   /*     Flux<String> flux =
                Flux.<String>error(new IllegalArgumentException())
                        .retryWhen(companion -> companion
                                .zipWith(Flux.range(1, 4),
                                         (error, index) -> {
                                             if (index < 4) return index;
                                             else throw Exceptions.propagate(error);
                                         })
                        );

        flux.log().subscribe();*/

     /* Flux.just(1,2,3,5,8,13,21).flatMapIterable(data->{
          List<Integer> result = Lists.newArrayList();


      }) ;
*/



   /*     Flux.just("1").concatMap(data->{
            System.out.println(data);
            return Flux.just(Integer.parseInt(data));

        }).subscribe();*/

/*   File root = new File ("D:\\CommonSofts");

   Flux.just(root).expand(parent->{
       File [] child = parent.listFiles();
       if (parent.listFiles()!=null){
           return Flux.just(child);
       }
       return Flux.empty();

   }).doOnNext(file -> {
       if(file!=null){
           System.out.println(file.getAbsolutePath());
       }

   }).subscribe();*/




      /*  Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
                .doOnError(System.out::println)
                 .retry(3);
                //.retryWhen(companion -> companion.take(3));
        flux.subscribe();*/




       /* Flux.interval(Duration.ofMillis(300), Schedulers.newSingle("test")).next().subscribe(data->{

            System.out.println(data);
        });
        Flux.interval(Duration.ofMillis(300)).subscribe(data->{

            System.out.println(data);
        });
*/


/*
        Flux <Integer> flux = Flux.just(1,2,3,4,5);

        List<Integer> arrays = Arrays.asList(1, 2, 3, 4);

        Flux flux2 = Flux.fromIterable(arrays);

        Flux flux3 = Flux.range(1,5).map(data ->{
            if(data==4){
                throw new RuntimeException("Got to 4");
            }

            return data*2;
        });*/


       /* flux2.subscribe(data->{
            System.out.println(data);
        },error->{
            System.out.println(error);
        },()->{
            System.out.println("end");
        });


        flux.subscribe(data->{
            System.out.println(data);
        }) ;

        flux3.subscribe(data-> {
            System.out.println(data);
        },error->{
            System.out.println(error.toString());
        });*/

/*
       Flux.generate(sink->{
           sink.next(10);
       }).subscribe(data->{System.out.println(data);});*/


       /* generateFlux().subscribe(data->{
            System.out.println(data);
        });*/


    }
}
