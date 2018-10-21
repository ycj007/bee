package com.ycj.bee.http;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ycj.bee.redis.RedisHelper;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Log4j2
public class HttpHelper {



    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class) ;
    private static OkHttpClient defaultClient;
    private static OkHttpClient clientWithAuth;
    private static Headers headers = Headers.of(
            "User-Agent",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/69.0.3497.81 Chrome/69.0.3497.81 Safari/537.36"





    );

    private static final ConcurrentHashMap<String,List<Cookie>> cookieStore = new ConcurrentHashMap<>();


    public static final MediaType JSON_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    enum ClientType {

        default_client, client_with_auth;


    }

    private static OkHttpClient getClient(ClientType clientType) {
        OkHttpClient okHttpClient = null;
        if (clientType != null) {
            switch (clientType) {

                case default_client:

                    if (defaultClient == null) {
                        okHttpClient = new OkHttpClient();
                        defaultClient = okHttpClient;

                    } else {
                        okHttpClient = defaultClient;
                    }
                    break;

                case client_with_auth:
                    if (clientWithAuth == null) {
                        okHttpClient = new OkHttpClient.Builder()
                                .cookieJar(new CookieJar() {
                                    @Override
                                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                                        if(list!=null) {
                                            //RedisHelper.set(httpUrl.host(), com.alibaba.fastjson.JSON.toJSONString(list));
                                            cookieStore.put(httpUrl.host(),list);
                                        }
                                    }

                                    @Override
                                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                                     /*   List<Cookie> result = Lists.newArrayList();
                                        if(RedisHelper.get(httpUrl.host())!=null){
                                            return  JSON.parseArray(RedisHelper.get(httpUrl.host()),Cookie.class);
                                        }else{
                                            return result;
                                        }*/

                                        List<Cookie> data = cookieStore.get(httpUrl.host());
                                        if(CollectionUtils.isEmpty(data)){
                                            return Lists.newArrayList();
                                        }
                                        return data;
                                    }
                                })
                                .build();
                        clientWithAuth = okHttpClient;
                    } else {
                        okHttpClient = clientWithAuth;
                    }

                    break;


            }
        }

        return okHttpClient;

    }

    public static OkHttpClient getDefaultClient() {

        return getClient(ClientType.client_with_auth);

    }

    public static  String get(String url) {
        Request request = new Request.Builder()
                .url(url).headers(headers)
                .build();
        try {
        Response response = getDefaultClient().newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            logger.error("get error",e);
        }

        return null;
    }


    public static  String post(String url, String json){

        RequestBody body = RequestBody.create(JSON_TYPE, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = null;
        try {
            response = getDefaultClient().newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            logger.error("post error",e);
        }

        return null;
    }


    public static File download (String url ){


        Set<PosixFilePermission> sets = new HashSet<>();
        sets.add(PosixFilePermission.OWNER_READ);
        sets.add(PosixFilePermission.OWNER_EXECUTE);
        sets.add(PosixFilePermission.OWNER_WRITE);

        File result = null;
        try {
              result = Files.createTempFile("test",".jpeg", PosixFilePermissions.asFileAttribute(sets)).toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        try {
            Response response = getDefaultClient().newCall(request).execute();



            FileUtils.copyInputStreamToFile(response.body().byteStream(),result);


        } catch (IOException e) {
            logger.error("download  error",e);
        }


        return  result;

    }


    public static void main(String[] args) {

       get("http://rlzyggfwzx.bjchy.gov.cn/Residence/a/login");
       File file = download("http://rlzyggfwzx.bjchy.gov.cn/Residence/servlet/validateCodeServlet?"+System.currentTimeMillis());

       File desc = new File("/home/ycj/test.jpeg");
        try {
            FileUtils.copyFile(file,desc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Tesseract instance = new Tesseract();
        instance.setDatapath("/home/ycj/桌面/Tess4J/tessdata");
        //将验证码图片的内容识别为字符串
        String result = null;
        try {
            result = instance.doOCR(desc);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        System.out.println(result);


    }


}
