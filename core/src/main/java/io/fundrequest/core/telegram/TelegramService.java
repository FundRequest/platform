package io.fundrequest.core.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

@Component
@ConditionalOnProperty("io.fundrequest.telegram.bot.enabled")
@Slf4j
public class TelegramService extends TelegramWebhookBot {

    private String botUsername;
    private String botToken;
    private String channel;
    private String prependText;

    public TelegramService(@Value("${io.fundrequest.telegram.bot.name}") String botUsername,
                           @Value("${io.fundrequest.telegram.bot.apikey}") String botToken,
                           @Value("${io.fundrequest.telegram.bot.fundrequest-internal-channel}") final String channel,
                           @Value("${io.fundrequest.telegram.bot.prepend-to-text}") final String prependText) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.channel = channel;
        this.prependText = prependText;
    }

    public void sendMessageToChannel(final String text) {
        try {
            this.execute(new SendMessage(channel, String.format("%s %s", prependText, text)));
        } catch (final Exception exc) {
            log.error("Unable to send message to telegram channel: {}", exc.getMessage());
        }
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return "/tmp/telegram_bot";
    }
}
