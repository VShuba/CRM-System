package ua.shpp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Service
@Slf4j
public class ImportClientsService {

    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().followRedirect(true)
            ))
            .build();

    public String importClients(String sheetId) {
        String initialUrl = "https://docs.google.com/spreadsheets/d/" + sheetId + "/export?format=tsv&gid=0";
        log.info("ImportClientsService uri: {}", initialUrl);

        String content = webClient.get()
                .uri(initialUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Content:\n{}", content);
        return content;
    }
}
