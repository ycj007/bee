package com.ycj.bee.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.io.Closeable;
import java.io.IOException;



public class RedisHelper  implements Closeable ,AutoCloseable{




    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static StatefulRedisConnection connection ;
    private static RedisClient redisClient = RedisClient.create(RedisURI.create(REDIS_HOST,REDIS_PORT));
    private static synchronized StatefulRedisConnection getConnection(){
        if(connection ==null){
            connection = redisClient.connect();
        }

        return connection;

    }



    @Override
    public void close() throws IOException {

        if(connection !=null){
            connection.close();

        }

        if(redisClient!=null){
            redisClient.shutdown();
        }

    }


    public static <T> T get(String key){
        RedisCommands<String, T> redisCommands =  getConnection().sync();
        return redisCommands.get(key);
    }

    public static <T> String set(String key,T t){
        RedisCommands<String, T> redisCommands =  getConnection().sync();
        return redisCommands.set(key,t);
    }




}
