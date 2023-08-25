package com.nan.jvm.obj.chat;

import lombok.Data;

@Data
public class Choice {

    private String text;
    private String index;
    private String logprobs;
    private String finish_reason;
    private Delta delta;


}
