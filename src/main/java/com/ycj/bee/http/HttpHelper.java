package com.ycj.bee.http;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Log4j2
public class HttpHelper {


    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    private static OkHttpClient defaultClient;
    private static OkHttpClient clientWithAuth;
    private static Headers headers = Headers.of(
            "User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"
            ,
            "Referer", "http://rlzyggfwzx.bjchy.gov.cn/Residence/a/login",
            "Origin", "http://rlzyggfwzx.bjchy.gov.cn",
            "Host", "rlzyggfwzx.bjchy.gov.cn",
            "Upgrade-Insecure-Requests", "1",
            "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"

    );

    private static final ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();


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
                                        if (list != null) {
                                            cookieStore.put(httpUrl.host(), list);
                                        }
                                    }

                                    @Override
                                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {


                                        List<Cookie> data = cookieStore.get(httpUrl.host());
                                        if (CollectionUtils.isEmpty(data)) {
                                            return Lists.newArrayList();
                                        }
                                        return data;
                                    }
                                }).addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request originalRequest = chain.request();


                                        Request reRequest = null;
                                        Request.Builder builder =       originalRequest.newBuilder()

                                                                                   .method(originalRequest.method(),originalRequest.body());


                                        headers.names()
                                               .forEach(k -> {
                                                   builder.addHeader(k, headers.get(k));
                                               });
                                        reRequest = builder.build();
                                        return chain.proceed(reRequest);

                                    }
                                })
                                .followRedirects(true)
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

    public static String get(String url) {
        Request request = null;
        Request.Builder builder = new Request.Builder()
                .url(url);

        request = builder.build();

        try {
            Response response = getDefaultClient().newCall(request)
                                                  .execute();

            return response.body()
                           .string();
        } catch (IOException e) {
            logger.error("get error", e);
        }

        return null;
    }


    public static String post(String url, String json) {

        RequestBody body = RequestBody.create(JSON_TYPE, json);

        Request request = null;

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        request = builder.build();


        Response response = null;
        try {
            response = getDefaultClient().newCall(request)
                                         .execute();

            return response.body()
                           .string();
        } catch (IOException e) {
            logger.error("post error", e);
        }

        return null;
    }


    public static String postForm(String url, Map<String, String> data) {
        RequestBody formBody = null;

        FormBody.Builder builder = new FormBody.Builder();

        data.forEach((k, v) -> {
            builder.add(k, v);
        });

        formBody = builder.build();


        Request request = null;

        Request.Builder requestBuilder =
                new Request.Builder()
                        .url(url)
                        .post(formBody);
        request = requestBuilder.build();

        try (Response response = getDefaultClient().newCall(request)
                                                   .execute()) {
            if (response.isSuccessful()) {


                return response.body()
                               .string();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static File download(String url, String suffix) {


        Set<PosixFilePermission> sets = new HashSet<>();
        sets.add(PosixFilePermission.OWNER_READ);
        sets.add(PosixFilePermission.OWNER_EXECUTE);
        sets.add(PosixFilePermission.OWNER_WRITE);

        File result = null;
        try {

            String realSuffix = ".jpg";
            String realPreffix = "download";
            if (StringUtils.isEmpty(suffix)) {
                realSuffix = suffix;
            }
            FileAttribute<Set<PosixFilePermission>> fileAttribute = PosixFilePermissions.asFileAttribute(sets);

            if (isLinux()) {

                result = Files.createTempFile(realPreffix, realSuffix, fileAttribute)
                              .toFile();
            } else if (isWindows()) {
                result = Files.createTempFile(realPreffix, realSuffix)
                              .toFile();
            } else {
                result = Files.createTempFile(realPreffix, realSuffix, fileAttribute)
                              .toFile();
            }

            result = Files.createTempFile(realPreffix, realSuffix)
                          .toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        try {
            Response response = getDefaultClient().newCall(request)
                                                  .execute();


            FileUtils.copyInputStreamToFile(response.body()
                                                    .byteStream(), result);


        } catch (IOException e) {
            logger.error("download  error", e);
        }


        return result;

    }

    private static boolean isLinux() {
        return System.getProperty("os.name")
                     .toLowerCase()
                     .indexOf("linux") >= 0;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name")
                     .toLowerCase()
                     .indexOf("win") >= 0;
    }


    private static String readValidateCode() {

        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据

        StringBuilder sb = new StringBuilder();
        // next方式接收字符串
        System.out.println("输入验证码：");
        // 判断是否还有输入
        if (scan.hasNext()) {
            String str = scan.next();
            sb.append(str);
        }
        scan.close();
        return sb.toString();

    }

    private static String getValidateCode(File desc, String dataPath) {
        Objects.requireNonNull(desc);
        Objects.requireNonNull(dataPath);

        Tesseract instance = new Tesseract();
        instance.setDatapath(dataPath);
        //将验证码图片的内容识别为字符串
        String result = null;
        try {
            result = instance.doOCR(desc);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {

        String index = "http://rlzyggfwzx.bjchy.gov.cn/Residence/a/login";
        String validateCode = "http://rlzyggfwzx.bjchy.gov.cn/Residence/servlet/validateCodeServlet?" + System.currentTimeMillis();
        String login = "http://rlzyggfwzx.bjchy.gov.cn/Residence/a/login";

        /**
         * 访问首页
         */

        get(index);
        /**
         * download validatecode
         */

        File file = download(validateCode, ".jpg");
        File descFile = new File("d://test.jpg");

        try {
            FileUtils.copyFile(file, descFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Map<String, String> data = Maps.newHashMap();
        data.put("username", "bu3023130");
        data.put("password", "Mtime123");
        String readValidateCode = readValidateCode();
        data.put("validateCode", readValidateCode);

        String result = postForm(login, data);
//        String result = posst(login, JSON.toJSONString(data));

        //System.out.println(result);
       // String requestUrl = "http://rlzyggfwzx.bjchy.gov.cn/Residence/a/rrs/agreement";
        //String requestUrl = "http://rlzyggfwzx.bjchy.gov.cn/Residence/a/rrs/query";
        String requestUrl = "http://rlzyggfwzx.bjchy.gov.cn/gdxw/501180.shtml";
        result = get(requestUrl);
        System.out.println(result);

    }


}
