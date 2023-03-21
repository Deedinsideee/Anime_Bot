package com.example.SpringBot.service;

import com.example.SpringBot.config.BotConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.List;
import java.util.Map;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messagetext = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messagetext) {
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;

                default:
                    handleMessage(chatId, update.getMessage().getText());
                    ;
                    break;
            }


        }
    }


    private void startCommand(long chatId, String name) {
        String answer = "Привет, " + name;
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String MessageToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(MessageToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }




        /*sendMessage(chatId,translation.getTranslatedText());*/





        private void handleMessage ( long chatId, String mess){

            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://kitsu.io/api/edge/anime").newBuilder();
            urlBuilder.addQueryParameter("filter[text]", mess);
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    // обработка ошибок
                }
                String responseBody = response.body().string();
                System.out.println(responseBody);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);


                JsonNode dataNode = rootNode.get("data");


                for (JsonNode animeNode : dataNode) {

                    try {

                        sendMessage(chatId, animeNode.get("attributes").get("slug").asText().replace('-',' '));
                        sendMessage(chatId, animeNode.get("attributes").get("synopsis").asText());
                        //translate(chatId,animeNode.get("attributes").get("synopsis").asText());
                        sendMessage(chatId, "--------------------------------------");

                    } catch (Exception e) {

                    }


                    // Выводим значения полей на экран

                }






           /* sendMessage(chatId, anime.slug);
            sendMessage(chatId, anime.synopsis);*/


                // здесь можно использовать полученные данные в формате JSON
            } catch (IOException e) {
                System.err.println(e.getMessage());
                ;
            }


            //OkHttpClient client = new OkHttpClient();
            //String url = "https://api.github.com/users/" + mess;
            //Request request = new Request.Builder()
            //                .url(url)
            //                .header("User-Agent", "OkHttp Example")
            //                .build();
            //try (Response response = client.newCall(request).execute()) {
            //        if (!response.isSuccessful()) {
            //                throw new IOException("Unexpected code " + response);
            //            }
            //        ObjectMapper objectMapper = new ObjectMapper();
            //        GitHubUser kekich = objectMapper.readValue(response.body().string(), GitHubUser.class);
            //        sendMessage(chatId, String.valueOf(kekich.id));
            //        sendMessage(chatId, kekich.login);
            //        sendMessage(chatId, kekich.html_url);
            //            /*String responseBody = response.body().string();
            //        System.out.println(responseBody);*/
            //    } catch (IOException e) {
            //        sendMessage(chatId, "такой пользователь не был найден");
            //    }


        }
    }





