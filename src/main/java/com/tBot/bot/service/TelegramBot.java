package com.tBot.bot.service;

import com.tBot.bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken(){
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        //главный метод. Здесь происходит обработка того, что шлёт пользователь

        if (update.hasMessage() && update.getMessage().hasText())//проверяем что чтото прислали && что это текст
        {
            String messageText = update.getMessage().getText();
            //Чтобы бот мог написать ему нужно знать чатID
            long chatID = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                        startCommandReceived(chatID, update.getMessage().getChat().getFirstName(), update.getMessage().getText());
                        break;
                default:
                    sendMessage(chatID, "Извини, но пока это не поддерживается");

            }
        }
    }
    private void startCommandReceived(long chatID, String name, String text){
        String answer = "Hi, " + name +", ты написал: " + text;
        log.info("Replied to user " + name);

        sendMessage(chatID, answer);
    }

    private void sendMessage(long chatID, String textToString){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText(textToString);

        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error("Error occured: " + e.getMessage());
        }
    }
}
