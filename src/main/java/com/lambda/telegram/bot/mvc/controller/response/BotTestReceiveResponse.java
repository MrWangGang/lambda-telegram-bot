package com.lambda.telegram.bot.mvc.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotTestReceiveResponse {
    private Long chat_id;
    private String text;
}
