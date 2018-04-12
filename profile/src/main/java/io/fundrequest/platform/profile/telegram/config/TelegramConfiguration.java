package io.fundrequest.platform.profile.telegram.config;

import io.fundrequest.platform.profile.telegram.bot.FundRequestVerifierBot;
import io.fundrequest.platform.profile.telegram.service.TelegramVerificationService;
import org.springframework.beans.factory.annotation.Value;
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
    public TelegramBotsApi provideTelegramBotsApi(@Value("${io.fundrequest.telegram.bot.name}")
                                                          String botName,
                                                  @Value("${io.fundrequest.telegram.bot.apikey}")
                                                          String botApiKey,
                                                  @Value("${io.fundrequest.telegram.bot.fundrequestChannel}")
                                                          String fundrequestChannel,
                                                  @Value("${io.fundrequest.telegram.bot.fundrequestChannelLink}")
                                                          String fundrequestChannelLink,
                                                  @Value("${io.fundrequest.telegram.bot.registrationPage}")
                                                          String registrationPage,
                                                  TelegramVerificationService telegramVerificationService) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new FundRequestVerifierBot(botApiKey, botName, telegramVerificationService, fundrequestChannel, fundrequestChannelLink, registrationPage));
        return telegramBotsApi;
    }
}
