package config;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class GatlingConfig {

    public static HttpProtocolBuilder chronolinkProtocol = http // Об'єкт для конфігурації HTTP-протоколу
            .baseUrl("http://ec2-34-254-199-54.eu-west-1.compute.amazonaws.com:8080") // Базовий URL сервера
            .acceptHeader("application/json") // Вказує, що клієнт очікує відповідь у форматі JSON
            .disableFollowRedirect(); // Вимикає автоматичне слідування за редиректами (важливо для перевірки статус-кодів)

    public static HttpProtocolBuilder localProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json");

}
