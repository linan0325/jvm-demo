package com.nan.jvm.chatgpt;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nan.jvm.obj.chat.Answer;
import com.nan.jvm.obj.chat.ChatGptArg;
import com.nan.jvm.obj.chat.Choice;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.support.AbstractAsyncRequesterConsumer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@Service
public class ChatApiTest {

    final static String ProxyHost="127.0.0.1";
    final static String ProxyPort="7890";

    @Autowired
    private ObjectMapper objectMapper;


    //String bodyString="{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"user\",\"content\":\"你好\"}],\"stream\":true}";

    public void GetChatAI() throws InterruptedException {

        while (true){

            Thread.sleep(8000);

            System.out.println("输入问题: ");
            String question = new Scanner(System.in).nextLine();

            GetAnswer(question);
        }

    }

    private CloseableHttpAsyncClient getHttpClient(){

        HttpHost httpHost = new HttpHost(ProxyHost,Integer.parseInt(ProxyPort));

        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(httpHost);

        return HttpAsyncClients.custom().setRoutePlanner(routePlanner).build();

    }

    private void GetAnswer(String questions){

        ChatGptArg chatGptArg = new ChatGptArg();
        chatGptArg.setModel("text-davinci-003");
        chatGptArg.setPrompt(questions);
        chatGptArg.setMax_tokens(7);
        chatGptArg.setTemperature(0);
//        String qu1="{\n" +
//                "  \"model\": \"text-davinci-003\",\n" +
//                "  \"prompt\":"+" \""+questions+"\",\n" +
//                "  \"max_tokens\": 70,\n" +
//                "  \"temperature\": 0\n" +
//                "}";

        CloseableHttpAsyncClient AsyncClient =getHttpClient();
        AsyncClient.start();
        AsyncRequestBuilder postRequest = AsyncRequestBuilder.post("https://api.openai.com/v1/completions");
        postRequest.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
        postRequest.setHeader(HttpHeaders.AUTHORIZATION,"Bearer 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");
       // postRequest.setHeader(HttpHeaders.AUTHORIZATION,"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");
        String s=null;
        try {
             s = objectMapper.writeValueAsString(chatGptArg);
            System.out.println("s: "+s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        postRequest.setEntity(s);
//        ContentType contentType = ContentType.create("text/plain", StandardCharsets.UTF_8);
//        postRequest.setEntity(s, contentType);
//        postRequest.setCharset(StandardCharsets.UTF_8);


        AbstractCharResponseConsumer<HttpResponse> responseConsumer = new AbstractCharResponseConsumer<HttpResponse>(){

            HttpResponse httpResponse;

            @Override
            public void releaseResources() {

            }

            @Override
            protected int capacityIncrement() {
                return Integer.MAX_VALUE;
            }

            @Override
            protected void data(CharBuffer charBuffer, boolean b) throws IOException {

                String answerFormChatGpt = charBuffer.toString();
                System.out.println("answerFormChatGpt:"+answerFormChatGpt);
                Answer answer = JSON.parseObject(answerFormChatGpt, Answer.class);

                List<Choice> choices = answer.getChoices();
                for (Choice choice :choices){
                    String text = choice.getText();
                    System.out.println("answer from chatGpt(test): "+text);
                }

            }

            @Override
            protected void start(HttpResponse httpResponse, ContentType contentType) throws HttpException, IOException {

                this.httpResponse=httpResponse;
            }

            @Override
            protected HttpResponse buildResult() throws IOException {
                return httpResponse;
            }
        };

        AsyncClient.execute(postRequest.build(), responseConsumer, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                System.out.println("此次问题回答结束");
            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void cancelled() {

            }
        });


    }

}
