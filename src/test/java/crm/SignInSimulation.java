package crm;

import io.gatling.javaapi.core.*;

import static config.GatlingConfig.chronolinkProtocol;
import static config.GatlingConfig.localProtocol;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class SignInSimulation extends Simulation { // Клас, що наслідує Simulation – точка входу для Gatling-тесту

    ScenarioBuilder signInScenario = scenario("CRM Sign In Test") // Створення сценарію з назвою "CRM Sign In Test"
            .exec( // Виконати HTTP-запит
                    http("Sign In Request") // Назва запиту для логування та звітів
                            .post("/auth/sign-in") // HTTP POST-запит до ендпоінту авторизації
                            .header("Content-Type", "application/json") // Заголовок, що вказує на формат тіла запиту
                            .body(StringBody("{\"login\": \"test@gmail.com\", \"password\": \"my_1secret1_password\"}")) // Тіло запиту у форматі JSON з логіном і паролем
                            .check(status().is(200)) // Перевірка, що відповідь має статус 200 (OK)
                            .check(jsonPath("$.token").exists()) // Перевірка, що у відповіді існує поле "token"
                            .check(jsonPath("$.token").saveAs("jwtToken")) // Збереження значення токена у змінну сесії "jwtToken"
            )
            .exec(session -> { // Обробка сесії після попереднього запиту
                String token = session.getString("jwtToken"); // Отримання токена з сесії
                System.out.println(" JWT Token: " + token); // Виведення токена у консоль
                return session; // Повернення оновленої сесії
            })
            .exec( // Виконання ще одного запиту
                    http("Access Protected Endpoint") // Назва запиту для логування
                            .get("/api/organizations") // GET-запит до захищеного ендпоінту
                            .header("Authorization", "Bearer #{jwtToken}") // Передача JWT токена у заголовку авторизації
                            .check(status().is(200)) // Перевірка, що відповідь має статус 200 (доступ дозволений)
            );

    {
        setUp( // Налаштування сценарію для виконання
                signInScenario.injectOpen(atOnceUsers(1)) // Симуляція запуску одного користувача одразу
        ).protocols(chronolinkProtocol); // Вказання HTTP-протоколу, який буде використано в тесті
    }
}
