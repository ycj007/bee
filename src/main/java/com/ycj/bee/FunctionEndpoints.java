package com.ycj.bee;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Flux;
import reactor.ipc.netty.http.server.HttpServer;

import java.nio.IntBuffer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@SpringBootApplication
public class FunctionEndpoints implements WebFluxConfigurer {

    public static void main(String[] args) {



        HttpServer httpServer = HttpServer.create(8080);

        MyHandler myHandler1 = new MyHandler();
          ;

        HttpHandler httpHandler =  RouterFunctions.toHttpHandler(RouterFunctions.route(GET("/my"), myHandler1::listPeople));
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
     /*   httpServer.startRouterAndAwait(httpServerRouter->{

            httpServerRouter.get("/my2",(request,response)->{



               return response.status(200).sendString(Flux.just("1","2","3"));
            });
        });*/
        httpServer.newHandler(adapter).block();



       /* Router router = new Router();

       HttpHandler httpHandler =  RouterFunctions.toHttpHandler(router.route(null));
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);


        HttpServer httpServer = HttpServer.builder().port(8080).build();
        httpServer.newHandler(adapter).block();



        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

      /*  HttpServer httpServer =  HttpServer.create(8080);
        httpServer.newHandler(adapter).block();*/
    }



  /*  public static void main(String[] args) {


        HttpHandler handler =

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        HttpServer.create(8080).newHandler(adapter).block();
    }*/






        // ...

      /*  @Override
        public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
            // configure message conversion...
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            // configure CORS...
        }

        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            // configure view resolution for HTML rendering...
        }*/

}
