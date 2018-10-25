package com.ycj.bee.controller;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;


/**
 * 异步请求
 */
@Controller
public class AsynController {


    private static final Map<String, DeferredResult<String>> map = Maps.newHashMap();



    @GetMapping("/download")
    public StreamingResponseBody streamingResponseBody() {
        return new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                // write...

                File file = new File("d://banner.jpg");

                FileUtils.copyFile(file,outputStream);
            }
        };
    }

    @GetMapping("/serverSentEventFlux")
    @ResponseBody
    public Flux<ServerSentEvent> serverSentEventFlux() {
        Flux<ServerSentEvent> deferredResult =  Flux.empty();
        // Save the deferredResult somewhere..



        Executors.newCachedThreadPool()
                 .execute(() -> {

                     try {
                         Thread.sleep(10000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     deferredResult.just(1,2,3,4,5).subscribe();

                 });
        return deferredResult;
    }

    @GetMapping("/deferr")
    @ResponseBody
    public DeferredResult<String> quotes() {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        // Save the deferredResult somewhere..

        map.put("data", deferredResult);

        Executors.newCachedThreadPool()
                 .execute(() -> {

                     try {
                         Thread.sleep(10000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     deferredResult.setResult("test");

                 });
        return deferredResult;
    }

    @GetMapping("/callable")
    public Callable<String> processUpload() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Callable<String>() {
            public String call() throws Exception {
                // ...
                return "/test/list";
            }
        };

    }

    @GetMapping("/events")
    public ResponseBodyEmitter handle() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        // Save the emitter somewhere..
        Executors.newCachedThreadPool()
                 .execute(() -> {

                     // In some other thread
                     try {
                         emitter.send("Hello once"+ DateFormatUtils.format(new Date(),DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern() ));
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     try {
                         Thread.sleep(3000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
// and again later on
                     try {
                         emitter.send("Hello again"+ DateFormatUtils.format(new Date(),DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern() ));
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     try {
                         Thread.sleep(3000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
// and done at some point
                     emitter.complete();
                     try {
                         Thread.sleep(3000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }


                 });
        return emitter;
    }
    @GetMapping("/sseevents")
    public SseEmitter sseEmitter() {
        SseEmitter emitter = new SseEmitter();
        // Save the emitter somewhere..
        Executors.newCachedThreadPool()
                 .execute(() -> {

                     // In some other thread
                     try {
                         emitter.send("Hello once"+ DateFormatUtils.format(new Date(),DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern() ));
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     try {
                         Thread.sleep(3000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
// and again later on
                     try {
                         emitter.send("Hello again"+ DateFormatUtils.format(new Date(),DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern() ));
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     try {
                         Thread.sleep(3000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
// and done at some point
                     emitter.complete();
                     try {
                         Thread.sleep(3000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }


                 });
        return emitter;
    }




}
