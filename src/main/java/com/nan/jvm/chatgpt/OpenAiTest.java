package com.nan.jvm.chatgpt;

import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

@Service
public class OpenAiTest {

    private static String ChatUrl="https://api.openai.com/v1/chat/completions";
    private static String KeyApi="Bearer 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";

    private void GetChatGptForChat(String questions){

        OpenAiService openAiApi = new OpenAiService(KeyApi);
        CompletionRequest completionRequest=CompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .echo(true)
                .user("test")
                .maxTokens(20)
                .n(3)
                .build();


        openAiApi.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        CreateImageRequest imageRequest = CreateImageRequest.builder().prompt(questions).build();

    }
}
