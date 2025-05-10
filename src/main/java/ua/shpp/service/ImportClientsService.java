package ua.shpp.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ua.shpp.dto.ClientRequestDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportClientsService {

    private final CsvMapper csvMapper;

    //todo розібратися restclient - webclient
    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().followRedirect(true)
            ))
            .build();

    public List<ClientRequestDto> importClients(String sheetId) {
        String initialUrl = "https://docs.google.com/spreadsheets/d/" + sheetId + "/export?format=tsv&gid=0";
        log.info("ImportClientsService uri: {}", initialUrl);

        String content = webClient.get()
                .uri(initialUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Content:\n{}", content);
        return parseTsvWithJackson(content);
    }


    //todo додати хендл всіх форматів дат( або хоча б більшості)
    private List<ClientRequestDto> parseTsvWithJackson(String tsvContet) {
        List<ClientRequestDto> validClients = new ArrayList<>();
        CsvSchema csvSchema = CsvSchema.builder()
                .setColumnSeparator('\t')
                .setUseHeader(true)
                .build();
            try (MappingIterator<ClientRequestDto> iterator = csvMapper.readerFor(ClientRequestDto.class)
                    .with(csvSchema)
                    .readValues(tsvContet)) {
                while (iterator.hasNext()) {
                    ClientRequestDto clientRequestDto = iterator.next();

                    if (isValid(clientRequestDto)){
                        validClients.add(clientRequestDto);
                    } else {
                        log.warn("Invalid client data: {}", clientRequestDto);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return validClients;
    }

    private boolean isValid(@Valid ClientRequestDto clientRequestDto) {
        return clientRequestDto != null;
    }
}
