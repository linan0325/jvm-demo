package com.nan.jvm.obj.chat;

import lombok.Data;

import java.util.List;

@Data
public class Answer {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;




}
