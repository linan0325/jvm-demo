package com.nan.jvm.obj.chat;

import lombok.Data;

@Data
public class Usage {

    private String prompt_tokens;
    private String completion_tokens;
    private String total_tokens;

}
