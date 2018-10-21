package com.ycj.bee;

import com.ycj.bee.redis.RedisHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeeApplication.class, args);
    }



    @Bean(destroyMethod="close")
    public RedisHelper redisHelper(){

        return new RedisHelper();
    }
}
