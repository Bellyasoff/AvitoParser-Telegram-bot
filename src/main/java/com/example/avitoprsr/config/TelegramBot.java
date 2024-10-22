package com.example.avitoprsr.config;

import com.example.avitoprsr.service.Parser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String text = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/help")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else {
                try {
                    sendMessage(chatId, "Обрабатываю ваш запрос...");
                    text = Parser.getUrl(messageText);
                } catch (Exception e) {
                    sendMessage(chatId, "Невозможно обработать запрос \n " +
                            "Попробуйте написать что-то другое");
                }
                sendMessage(chatId, text);
                sendMessage(chatId, "Вот, что мне удалось найти по запросу \"" + messageText + "\"");
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Привет, " + name + "!" + "\n" +
                "Введи запрос, а я пришлю тебе объявления с сайта Авито" + "\n" +
                "Например: Apple MacBook Pro";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendMessage(chatId, "Что-то пошло не так.");
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
