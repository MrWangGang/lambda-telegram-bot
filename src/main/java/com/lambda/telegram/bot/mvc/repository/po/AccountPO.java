package com.lambda.telegram.bot.mvc.repository.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("account")
public class AccountPO {
    @Id
    private Long id;

    private String username;

    private Double amount;

    private String model;

    private LocalDateTime createTime;
}
