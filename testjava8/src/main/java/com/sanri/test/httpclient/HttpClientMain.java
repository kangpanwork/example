package com.sanri.test.httpclient;

import com.alibaba.fastjson.JSON;
import com.sanri.test.httpclient.dto.ContentTypeDto;
import com.sanri.test.httpclient.handler.JsonResponseHandler;
import com.sanri.test.httpclient.handler.StreamReponseHandler;
import com.sanri.test.reflect.RandomDataService;
import com.sanri.test.reflect.RandomUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HttpClientMain {

    /**
     * 如果只是请求一个 html 页,好像 https 和 http 没区别, 如果要获取资源,如文件,则会出现证书问题
     * @throws IOException
     */
    @Test
    public void testHttps() throws IOException {
        // 解决 unable to find valid certification path to requested target
        TrustManager[] trustAllCerts = new TrustManager[]{new URLResourceMain.CustomTrustManager()};
        HttpClientConnectionManager connManager = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, trustAllCerts, null);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    // AllowAllHostnameVerifier 用于解决 httpclient hostname in certificate didn't match:
                    .register("https", new SSLConnectionSocketFactory(sslContext,new AllowAllHostnameVerifier()) )
                    .build();
            connManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);

        }catch (NoSuchAlgorithmException | KeyManagementException  | NoSuchProviderException e){
            e.printStackTrace();
        }

        HttpGet httpGet = new HttpGet("https://10.101.70.202/scp-st-informationreleaseapp/20200622/15927903519247umbDuff66.mp4");
        CloseableHttpClient minimal = HttpClients.custom().setConnectionManager(connManager).build();
        CloseableHttpResponse execute = minimal.execute(httpGet);
        HttpEntity entity = execute.getEntity();
        InputStream content = entity.getContent();
        long copy = Files.copy(content, Paths.get("d:/test", "m4m.mp4"));
        System.out.println(copy);
    }

    @Test
    public void testResponseHandler() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse execute = httpClient.execute(httpGet);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String handleResponse = responseHandler.handleResponse(execute);
        System.out.println(handleResponse);

        HttpClientUtils.closeQuietly(execute);
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Test
    public void testJsonResponseHandler() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest httpGet = new HttpGet("http://10.101.72.42:8081/sanritools/index/listTools");
        CloseableHttpResponse execute = httpClient.execute(httpGet);

        ResponseHandler<JSON> responseHandler = new JsonResponseHandler();
        JSON handleResponse = responseHandler.handleResponse(execute);
        System.out.println(handleResponse);

        HttpClientUtils.closeQuietly(execute);
        HttpClientUtils.closeQuietly(httpClient);
    }

    @Test
    public void testStreamResponseHandler() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest httpGet = new HttpGet("http://10.101.72.42:8081/sanritools/index/listTools");
        CloseableHttpResponse execute = httpClient.execute(httpGet);

        ResponseHandler<InputStream> responseHandler = new StreamReponseHandler();
        InputStream inputStream = responseHandler.handleResponse(execute);
        IOUtils.copy(inputStream,new FileOutputStream("d:/test/xx.json"));

        HttpClientUtils.closeQuietly(execute);
        HttpClientUtils.closeQuietly(httpClient);
    }

    /**
     * 提交表单数据
     */
    @Test
    public void postFormData() throws IOException {
        CloseableHttpClient minimal = HttpClients.createMinimal();

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("name","测试名称"));
        String format = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
        parameters.add(new BasicNameValuePair("beginTime",format ));
        parameters.add(new BasicNameValuePair("status","1"));
        HttpEntity httpEntity = new UrlEncodedFormEntity(parameters,"utf-8");

        HttpPost httpPost = new HttpPost("http://localhost:8082/httpclient/postFormData");
        httpPost.setEntity(httpEntity);

        JSON execute = minimal.execute(httpPost, new JsonResponseHandler());
        System.out.println(execute);
    }
    RandomDataService randomDataService = new RandomDataService();

    /**
     * 提交 json 数据
     * @throws IOException
     */
    @Test
    public void postJsonData() throws IOException {
        CloseableHttpClient minimal = HttpClients.createMinimal();

        Object o = randomDataService.populateData(ContentTypeDto.class);
        HttpEntity httpEntity = new StringEntity(JSON.toJSONString(o),ContentType.APPLICATION_JSON.withCharset("utf-8"));

        HttpPost httpPost = new HttpPost("http://localhost:8082/httpclient/postJsonData");
        httpPost.setEntity(httpEntity);

        JSON execute = minimal.execute(httpPost, new JsonResponseHandler());
        System.out.println(execute);
    }

    /**
     * 请求头的数据
     */
    @Test
    public void withHeaderData() throws IOException {
        CloseableHttpClient minimal = HttpClients.createMinimal();

        HttpPost httpPost = new HttpPost("http://localhost:8082/httpclient/header");
        httpPost.addHeader("token", UUID.randomUUID().toString());

        JSON execute = minimal.execute(httpPost, new JsonResponseHandler());
        System.out.println(execute);
    }

    /**
     * 多部分上传
     */
    @Test
    public void testMultipart() throws IOException {
        CloseableHttpClient minimal = HttpClients.createMinimal();

        HttpEntity httpEntity = MultipartEntityBuilder.create().addTextBody("idcard", RandomUtil.idcard())
                .addPart("file", new FileBody(new File("d:/sanritoolsplugins.rar"))).build();

        HttpPost httpPost = new HttpPost("http://localhost:8082/httpclient/post2Multipart");
        httpPost.setEntity(httpEntity);

        JSON execute = minimal.execute(httpPost, new JsonResponseHandler());
        System.out.println(execute);

        HttpClientUtils.closeQuietly(minimal);
    }

}
