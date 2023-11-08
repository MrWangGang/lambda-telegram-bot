package com.lambda.telegram.bot.mvc.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewPanel {
    String model = "NaN";
    String income = "0.00";
    String distribute = "0.00";
    String rate = "0.00";
    String ruAmount = "0.00";
    String yingAmount = "0.00";
    String yiAmount = "0.00";
    String weiAmount = "0.00";
    String shouAmount = "0.00";
    String date = "YYYY-MM-DD";
}
