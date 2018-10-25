package com.ycj.bee;

import com.ycj.bee.redis.RedisHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.concurrent.Callable;

@SpringBootApplication
public class BeeApplication  {

    public static void main(String[] args) {
        SpringApplication.run(BeeApplication.class, args);
    }



    @Bean(destroyMethod="close")
    public RedisHelper redisHelper(){

        return new RedisHelper();
    }



    @Bean
    public WebMvcConfigurer webMvcConfigurer(){

        return  new WebMvcConfigurer() {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
               // configurer.setTaskExecutor(new ThreadPoolTaskExecutor());
                configurer.setDefaultTimeout(20000);
                configurer.registerCallableInterceptors(new CallableProcessingInterceptor() {
                    @Override
                    public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
                        System.out.println("beforeConcurrentHandling--------------------------");
                    }

                    @Override
                    public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {
                        System.out.println("preProcess--------------------------");
                    }

                    @Override
                    public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {
                        System.out.println("postProcess--------------------------");
                    }

                    @Override
                    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
                        System.out.println("handleTimeout--------------------------");
                        return null;
                    }

                    @Override
                    public <T> Object handleError(NativeWebRequest request, Callable<T> task, Throwable t) throws Exception {
                        System.out.println("handleTimeout--------------------------");
                        return null;
                    }

                    @Override
                    public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {

                    }
                });
                configurer.registerDeferredResultInterceptors(new DeferredResultProcessingInterceptor(){

                    @Override
                    public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
                        System.out.println("beforeConcurrentHandling--------------------------");
                    }

                    @Override
                    public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
                        System.out.println("preProcess--------------------------");
                    }

                    @Override
                    public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult) throws Exception {
                        System.out.println("postProcess--------------------------");
                    }

                    @Override
                    public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
                        System.out.println("handleTimeout--------------------------");
                        return false;
                    }

                    @Override
                    public <T> boolean handleError(NativeWebRequest request, DeferredResult<T> deferredResult, Throwable t) throws Exception {
                        System.out.println("handleError--------------------------");
                        return false;
                    }

                    @Override
                    public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
                        System.out.println("afterCompletion--------------------------");
                    }
                });


            }
        };
    }


}
