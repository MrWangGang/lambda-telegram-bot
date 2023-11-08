package com.lambda.telegram.bot;

import org.lambda.framework.rpc.Rpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.lambda.telegram.bot","org.lambda.framework"} )
public class Run{
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Run.class, args);
    }
}
