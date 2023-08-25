package com.nan.jvm.obj.chat;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatCptArg2 {

    private String model;
    private List<Message> messages = new ArrayList<>();

    private boolean stream = true;
    public void addMessages(Message message) {
        this.messages.add(message);
    }
}
