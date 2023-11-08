package com.lambda.telegram.bot.mvc.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BotTestReceiveRequest {
    private Long update_id;
    private TelegramMessage message;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TelegramMessage {
        private Long message_id;
        private TelegramUser from;
        private TelegramChat chat;
        private Date date;
        private String text;
        private List<TelegramEntity> entities;

        // Getters and setters

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TelegramUser {
            private Long id;
            private Boolean is_bot;
            private String first_name;
            private String username;
            private String language_code;

            // Getters and setters
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TelegramChat {
            private Long id;
            private String first_name;
            private String username;
            private String type;

            // Getters and setters
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TelegramEntity {
            private Long offset;
            private Long length;
            private String type;

            // Getters and setters
        }
    }
}

