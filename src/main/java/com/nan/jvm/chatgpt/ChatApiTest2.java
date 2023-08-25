package com.nan.jvm.chatgpt;

import com.alibaba.fastjson.JSON;
import com.nan.jvm.obj.chat.*;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.var;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static java.lang.System.out;

@Service
public class ChatApiTest2{

    @Resource(name="ChatGptHttpClient")
    private CloseableHttpClient closeableHttpClient;

   private static String ChatUrl="https://api.openai.com/v1/chat/completions";
    private final Charset charset = StandardCharsets.UTF_8;

   public void GetChatGpt(){


//       try{
//
//           Thread.sleep(2000);
//
//           System.out.println("输入问题: ");
//           String question = new Scanner(System.in).nextLine();
//
//           String Answers = GetAnswerFromChatGpt(question);
//           System.out.println("Answers: "+Answers);
//
//       }catch (Exception e) {
//
//          e.printStackTrace();
//       }

       while (true){
           try{

               Thread.sleep(3000);

               out.println("");

               System.out.print("输入问题: ");

               String question = new Scanner(System.in).nextLine();

              // String Answers = GetAnswerFromChatGpt(question);
               GetAnswerFromChatGpt(System.out::print,question);

               //System.out.println("Answers: "+Answers);

           }catch (Exception e){

               e.printStackTrace();
           }

       }

   }


   private String GetAnswerFromChatGpt(Consumer<String> consumer,String questions) throws IOException, ParseException {

       ChatCptArg2 chatCptArg2 = new ChatCptArg2();
       chatCptArg2.setModel("gpt-3.5-turbo");
       chatCptArg2.addMessages(new Message("user",questions));
       String ArgString= JSON.toJSONString(chatCptArg2);
       //System.out.println("ArgString:"+ArgString);

       HttpPost httpPost = new HttpPost(ChatUrl);

       httpPost.setHeader(HttpHeaders.CONTENT_TYPE,"application/json");
       httpPost.setHeader(HttpHeaders.AUTHORIZATION,"Bearer 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");

       httpPost.setEntity(new StringEntity(ArgString,StandardCharsets.UTF_8));


       CloseableHttpResponse response = closeableHttpClient.execute(httpPost);


       int code = response.getCode();
       //System.out.println("code: "+code);

       String AnswerFormAI = EntityUtils.toString(response.getEntity(),charset);
       //System.out.println("AnswerFormAI:"+AnswerFormAI);
      AnswerFormAI=AnswerFormAI.replaceAll("\\\n","").toString();


       String[] dataArray = AnswerFormAI.split("data: ");
      // String[] dataArray = AnswerFormAI.split("\\\n");

       StringBuffer Answers = new StringBuffer();

       CountDownLatch latch = new CountDownLatch(1);

       for(String data: dataArray){
           if(null ==data || "".equals(data) || !data.startsWith("{") || "null".equals(data)) continue;
           //System.out.println("data:"+data);
           ChatData chatData = JSON.parseObject(data, ChatData.class);
           //System.out.println("chatData:"+chatData.toString());
           List<Choice> choices = chatData.getChoices();
           Choice choice = choices.get(0);
           String content = choice.getDelta().getContent();
           if("null".equals(content)|| null==content)continue;

           //System.out.println(content);

           Answers.append(content);
           try {
               Thread.sleep(70);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           consumer.accept(content);

       }
       //latch.countDown();
       return Answers.toString();
   }
}
