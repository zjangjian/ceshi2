package com.example.jianzhang.myapplication33.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.jianzhang.myapplication33.R;
import com.example.jianzhang.myapplication33.UHFApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tiansj on 15/2/27.
 */
public class HttpClient {

    private static final int CONNECT_TIME_OUT = 5;
    private static final int WRITE_TIME_OUT = 60;
    private static final int READ_TIME_OUT = 60;
    private static final int MAX_REQUESTS_PER_HOST = 10;
    private static final String TAG = HttpClient.class.getSimpleName();
    private static final String UTF_8 = "UTF-8";
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    public static final int PAGE_SIZE = 10;
    private static OkHttpClient client;


    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new LoggingInterceptor());
//        builder.cookieJar(new JavaNetCookieJar());
        client = builder.build();
        client.dispatcher().setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);
    }

    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.i(TAG, String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) UHFApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Log.v("ConnectivityManager", e.getMessage());
        }
        return false;
    }

    public static void get(String url, Map<String, String> param, final HttpResponseHandler httpResponseHandler) {
        if (!isNetworkAvailable()) {
            Toast.makeText(UHFApplication.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        if (param != null && param.size() > 0) {
            url = url + "?" + mapToQueryString(param);
        }
        Request request = new Request.Builder()
                .url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                httpResponseHandler.sendSuccessMessage(response);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                httpResponseHandler.sendFailureMessage(call.request(), e);
            }
        });
    }

    /**
     * post请求
     *
     * @param url     请求地址
     * @param param   请求参数
     * @param handler 发送请求成功或失败的消息
     */
    public static void post(String url, Map<String, Object> param, final HttpResponseHandler handler) {
        if (!isNetworkAvailable()) {
            Toast.makeText(UHFApplication.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        String paramStr = "";
        if (param != null && param.size() > 0) {
//            paramStr = Base64Util.encode(JSON.toJSONString(param));
            paramStr = JSON.toJSONString(param);
        }
        Request request;
//        RequestBody body = RequestBody.create(MEDIA_TYPE, paramStr);
//        request = new Request.Builder().url(url).post(body).build();

        RequestBody body = getRequestBody(param);
        if (url.contains("oauth/token")) {
            request = new Request.Builder()
                    .header("Authorization", "Basic Y2xpZW50X2lkOmNsaWVudF9zZWNyZXQ=")
//                    .addHeader("Content-type", "application/json ;charset=utf-8")
                    .url(url).post(body).build();

        } else {
            request = new Request.Builder()
                    .url(url).post(body).build();
        }


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                handler.sendSuccessMessage(response);

            }

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendFailureMessage(call.request(), e);
            }
        });

    }

    public static String mapToQueryString(Map<String, String> map) {
        StringBuilder string = new StringBuilder();
        /*if(map.size() > 0) {
            string.append("?");
        }*/
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                string.append(entry.getKey());
                string.append("=");
                string.append(URLEncoder.encode(entry.getValue(), UTF_8));
                string.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String requestString = string.toString().substring(0, string.length() - 1);
        return requestString;
    }


    public static RequestBody getRequestBody(Map<String, Object> map) {
        StringBuilder string = new StringBuilder();
        /*if(map.size() > 0) {
            string.append("?");
        }*/

        FormBody.Builder builder = new FormBody.Builder();
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                builder.add(entry.getKey(), URLEncoder.encode((String) entry.getValue(), UTF_8));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return builder.build();
    }


    /**
     * 初始化HTTPS,添加信任证书
     *
     * @param context
     */
    public static OkHttpClient.Builder initHttps(Context context) {
//        X509TrustManager trustManager;
//        SSLSocketFactory sslSocketFactory;
//        final InputStream inputStream;
//        try {
//            inputStream = context.getAssets().open("srca.cer"); // 得到证书的输入流
//            try {
//
//                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                trustStore.load(inputStream, null);
//                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//                trustManagerFactory.init(trustStore);
//              //  trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
//                SSLContext sslContext = SSLContext.getInstance("TLS");
//                sslContext.init(null,trustManagerFactory.getTrustManagers(), null);
//                sslSocketFactory = sslContext.getSocketFactory();
//
//            } catch (GeneralSecurityException e) {
//                throw new RuntimeException(e);
//            }
//
////            client = new OkHttpClient.Builder()
////                    .sslSocketFactory(sslSocketFactory)
////                    .build();
//            OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                    .sslSocketFactory(sslSocketFactory);
//            return builder;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return  new OkHttpClient.Builder();
//        }

        SSLContext sslContext = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream cerInputStream = context.getAssets().open("srca.cer");
            Certificate ca = cf.generateCertificate(cerInputStream);

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory());
            return builder;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new OkHttpClient.Builder();
    }


    public static final class JavaNetCookieJar implements CookieJar {
        private final List<Cookie> allCookies = new ArrayList<Cookie>();

        @Override
        public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            allCookies.addAll(cookies);
            Log.e("saveFromResponse", ":" + allCookies.toString());
        }

        @Override
        public synchronized List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> result = new ArrayList<Cookie>();
            for (Cookie cookie : allCookies) {
                if (cookie.matches(url)) {
                    result.add(cookie);
                }
            }

            Log.e("List<Cookie>", ":" + result.toString());
            return result;
        }
    }
}
