package com.nan.jvm.obj.chat;

import lombok.Data;

@Data
public class ChatGptArg {
    private String model;
    private String prompt;
    private int max_tokens;
    private int temperature;

}
