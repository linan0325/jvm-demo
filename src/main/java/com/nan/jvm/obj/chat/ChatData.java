package com.nan.jvm.obj.chat;

import lombok.Data;

import java.util.List;


@Data
public class ChatData {
    private String id;
    private String object;
    private String created;
    private String model;
    private List<Choice> choices;


}
