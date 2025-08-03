package com.example.credit_product.bot;

import com.example.credit_product.service.RecommendationService;
import com.example.credit_product.dto.RecommendationDTO;
import com.example.credit_product.model.Users;
import com.example.credit_product.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Component
public class TelegramRecommendationBot {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final TelegramBot bot;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String BASE_URL = "http://localhost:8080";

    public TelegramRecommendationBot(
            RecommendationService recommendationService,
            UserRepository userRepository
    ) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
        this.bot = new TelegramBot("8281949747:AAH6knWGarudwggle1LFznYV72ft6GwsXSg");
    }

    @PostConstruct
    public void start() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                Message message = update.message();
                if (message != null && message.text() != null) {
                    String text = message.text();
                    long chatId = message.chat().id();

                    if ("/start".equalsIgnoreCase(text.trim())) {
                        bot.execute(new SendMessage(chatId, """

                                                                Привет! Я рекомендательный бот.
                                
                                                                Для получения персональных рекомендаций используйте команду:
                                /recommend username
                                
                                Дополнительные команды:
                                /rule_stats — статистика срабатывания правил
                                /clear_caches — сбросить кеш системы
                                /system_info — информация о сервисе
                                Привет! Я рекомендательный бот.
                                
                                Для получения персональных рекомендаций используйте команду:
/recommend username
                                
Дополнительные команды:
/rule_stats — статистика срабатывания правил
/clear_caches — сбросить кеш системы
/system_info — информация о сервисе

                                """));
                        continue;
                    }

                    if (text.startsWith("/recommend ")) {
                        String username = text.substring("/recommend ".length()).trim();
                        String response = handleRecommendation(username);
                        bot.execute(new SendMessage(chatId, response));
                    } else if ("/rule_stats".equalsIgnoreCase(text.trim())) {
                        String stats = getRuleStats();
                        bot.execute(new SendMessage(chatId, stats));
                    } else if ("/clear_caches".equalsIgnoreCase(text.trim())) {
                        String cleared = clearCaches();
                        bot.execute(new SendMessage(chatId, cleared));
                    } else if ("/system_info".equalsIgnoreCase(text.trim())) {
                        String info = getSystemInfo();
                        bot.execute(new SendMessage(chatId, info));
                    } else {
                        bot.execute(new SendMessage(chatId, """

                                                            Для получения персональных рекомендаций используйте команду:
                                /recommend username
                                
                                Дополнительные команды:
                                /rule_stats — статистика срабатывания правил
                                /clear_caches — сбросить кеш системы
                                /system_info — информация о сервисе
                                """));
=======
                            Для получения персональных рекомендаций используйте команду:
/recommend username

Дополнительные команды:
/rule_stats — статистика срабатывания правил
/clear_caches — сбросить кеш системы
/system_info — информация о сервисе
                        """));

                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private String handleRecommendation(String username) {
        Optional<Users> userOpt = userRepository.findByName(username);
        if (userOpt.isEmpty()) {
            return "Пользователь не найден.";
        }
        Users user = userOpt.get();
        List<RecommendationDTO> recs = recommendationService.getRecommendations(user.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("Здравствуйте, ").append(user.getName()).append("!\n\n");
        sb.append("Новые продукты для вас:\n");
        if (recs == null || recs.isEmpty()) {
            sb.append("(подходящих продуктов пока нет)");
        } else {
            for (RecommendationDTO dto : recs) {
                sb.append("• ").append(dto.getName());
                if (dto.getText() != null && !dto.getText().isBlank()) {
                    sb.append(": ").append(dto.getText());
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String getRuleStats() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/rule/stats"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return "Статистика срабатываний:\n" + response.body();
        } catch (Exception e) {
            return "Ошибка при получении статистики: " + e.getMessage();
        }
    }

    private String clearCaches() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/management/clear-caches"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return "Кеш успешно сброшен.";
        } catch (Exception e) {
            return "Ошибка при сбросе кеша: " + e.getMessage();
        }
    }

    private String getSystemInfo() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/management/info"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return "Информация о сервисе:\n" + response.body();
        } catch (Exception e) {
            return "Ошибка при получении информации о сервисе: " + e.getMessage();
        }
    }
}