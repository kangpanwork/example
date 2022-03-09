package com.sanri.test.httpclient;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * 使用默认的 url.openConnection 来获取数据
 * 需要支持绕过
 */
public class URLResourceMain {
    private String _12306 = "https://kyfw.12306.cn/";
    private String project = "https://10.101.70.202/scp-st-informationreleaseapp/20200622/15927903519247umbDuff66.mp4";
    private String login = "https://hhd-77-scp.evergrande.cn/login";

    /**
     * 用静态块解决 https 访问问题需要证书的问题
     */
    static {
        // 解决  No subject alternative names matching IP address 10.101.70.202 found
        HttpsURLConnection.setDefaultHostnameVerifier(new TrustAnyHostnameVerifier());

        // 解决 unable to find valid certification path to requested target
        TrustManager[] trustAllCerts = new TrustManager[]{new CustomTrustManager()};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }catch (NoSuchProviderException |  NoSuchAlgorithmException |  KeyManagementException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadResource() throws IOException {
        URL url = new URL(project);
        URLConnection urlConnection = url.openConnection();
        System.out.println(urlConnection instanceof HttpsURLConnection);
        InputStream inputStream = urlConnection.getInputStream();
        long copy = Files.copy(inputStream, Paths.get("d:/test", "m2m.mp4"));
        System.out.println(copy);
    }

    static class CustomTrustManager implements TrustManager, X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }
    }

    static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
