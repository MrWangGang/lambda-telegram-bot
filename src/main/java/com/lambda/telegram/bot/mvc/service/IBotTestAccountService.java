package com.lambda.telegram.bot.mvc.service;

import reactor.core.publisher.Mono;

public interface IBotTestAccountService {

    public Mono<String> processAccount(String userName, String message);

}
