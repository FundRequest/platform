package io.fundrequest.core.config;

import io.fundrequest.core.telegram.TelegramService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@Configuration
@ConditionalOnProperty("io.fundrequest.telegram.bot.enabled")
public class TelegramConfiguration {

    @Bean
    public TelegramBotsApi provideTelegramBotsApi(TelegramService telegramService) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(telegramService);
        return telegramBotsApi;
    }
}
