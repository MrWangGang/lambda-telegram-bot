package com.lambda.telegram.bot.mvc.repository.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("rate")
public class RatePO {
    @Id
    private Long id;

    private Double rate;

    private String model;

    private String username;
}
