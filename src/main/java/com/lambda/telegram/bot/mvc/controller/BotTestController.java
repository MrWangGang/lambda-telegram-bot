package com.lambda.telegram.bot.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.telegram.bot.mvc.controller.request.BotTestReceiveRequest;
import com.lambda.telegram.bot.mvc.service.IBotTestAccountService;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/telegram/bot/test")
public class BotTestController {
    Log log = LogFactory.getLog(BotTestController.class);

    @Resource
    private IBotTestAccountService iBotTestAccountService;
    @PostMapping("/receive")
    public Mono<Void> test(@RequestBody BotTestReceiveRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("request>>>>>>>>>>>>>>>>>>>>>"+objectMapper.writeValueAsString(request));
        Mono<String> message = iBotTestAccountService.processAccount(request.getMessage().getFrom().getUsername(),request.getMessage().getText());
        message.subscribe(result->{
            response(request.getMessage().getChat().getId(),result);
        });
        return Mono.empty();
    }
    public static RestTemplate restTemplate = new RestTemplate();
    public void response(Long chatId,String text){
        String url = "https://api.telegram.org/bot6771697548:AAEQ6MwvUpxLQ25e6y2XErrZLPzneEiyAxE/sendMessage";

        // 准备请求参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("chat_id", chatId.toString());
        params.add("text", text);
        params.add("parse_mode","Markdown");

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 创建HttpEntity对象，将请求参数和请求头合并
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // 执行POST请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 获取响应状态码和响应体
        int statusCode = responseEntity.getStatusCodeValue();
        String responseBody = responseEntity.getBody();

        log.info("response--->"+responseBody);

    }

}
