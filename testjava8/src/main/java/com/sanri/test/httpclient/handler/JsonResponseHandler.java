package com.sanri.test.httpclient.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

/**
 * json 响应处理
 */
@Slf4j
public class JsonResponseHandler implements ResponseHandler<JSON> {
    private ResponseHandler<String> basicResponse = new BasicResponseHandler();

    @Override
    public JSON handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        try {
            String handleResponse = basicResponse.handleResponse(response);
            if (StringUtils.isBlank(handleResponse)) {
                return null;
            }
            Object parse = JSON.parse(handleResponse);
            return (JSON) parse;
        }catch (HttpResponseException e){
            StatusLine statusLine = response.getStatusLine();
            log.error("[{}]",statusLine,e);
        }
        return null;
    }
}