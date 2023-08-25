package com.nan.jvm.chatgpt;


import net.sf.json.JSONObject;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;


import java.util.HashMap;

class OpenAiApiExample {

    public static void main(String[] args) throws Exception {

//        JSONObject requestBody = new JSONObject();
//        //requestBody.put("model","text-davinci-003");
//        requestBody.put("temperature",0);
//        requestBody.put("max_tokens",70);
//        //requestBody.put("top_p",1);
//        //requestBody.put( "frequency_penalty",0.0);
//        //requestBody.put("presence_penalty",0.6);
//        requestBody.put("prompt","讲一个笑话");

        String ProxyHost="127.0.0.1";
        String ProxyPort="7890";


        String bodyString="{\n" +
                "  \"model\": \"text-davinci-003\",\n" +
                "  \"prompt\": \"Java Stream list to map\",\n" +
                "  \"max_tokens\": 100,\n" +
                "  \"temperature\": 0.5\n" +
                "}";

        HttpHost proxy = new HttpHost(ProxyHost,ProxyPort);

        DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom().setRoutePlanner(defaultProxyRoutePlanner).build();


        CloseableHttpClient aDefault = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.openai.com/v1/completions");

        RequestConfig build = RequestConfig.custom().setProxy(proxy).build();
        httpPost.setConfig(build);


        //HttpPost httpPost = new HttpPost("https://api.openai.com/v1/engines/davinci/jobs");
        httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
        httpPost.setHeader(HttpHeaders.AUTHORIZATION,"Bearer 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");
        httpPost.setEntity(new StringEntity(bodyString));

        HttpResponse execute = aDefault.execute(httpPost);
        String s = execute.toString();

        System.out.println("s:"+s);
    }
}