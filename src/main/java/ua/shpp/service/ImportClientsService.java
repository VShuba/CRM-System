package ua.shpp.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ua.shpp.dto.ClientRequestDto;
import ua.shpp.exception.GoogleSheetsNotAuthorizedException;
import ua.shpp.exception.IllegalGoogleSheetFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportClientsService {

    private final CsvMapper csvMapper;
    private final ClientService clientService;
    private final Validator validator;

    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().followRedirect(true)
            ))
            .build();

    public void importClients(Long orgId, String sheetId) {
        String initialUrl = buildUrlToGoogleSheet(sheetId);
        String content = getTSVFromGoogleSheet(initialUrl);
        List<ClientRequestDto> parsedClients = parseTsvToClient(content);
        importClientsToOrganization(orgId, parsedClients);
    }

    private void importClientsToOrganization(Long orgId, List<ClientRequestDto> importedClients) {
        for (ClientRequestDto importedClientDto : importedClients) {
            clientService.createClient(orgId, importedClientDto);
        }
    }

    private static String buildUrlToGoogleSheet(String sheetId) {
        String initialUrl = "https://docs.google.com/spreadsheets/d/" + sheetId + "/export?format=tsv&gid=0";
        log.info("ImportClientsService uri: {}", initialUrl);
        return initialUrl;
    }

    private String getTSVFromGoogleSheet(String initialUrl) {
        String content = webClient.get()
                .uri(initialUrl)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new GoogleSheetsNotAuthorizedException("Forbidden access to sheet")))
                .bodyToMono(String.class)
                .block();
        log.debug("Received content:\n{}", content);
        return content;
    }


    private List<ClientRequestDto> parseTsvToClient(String tsvContet) {
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

                    if (isValid(clientRequestDto)) {
                        validClients.add(clientRequestDto);
                    } else {
                        log.warn("Invalid client data: {}", clientRequestDto);
                    }
                }
            } catch (RuntimeJsonMappingException e) {
                log.warn("Error parsing google sheet tsv: {}", tsvContet, e);
                throw new IllegalGoogleSheetFormatException("Illegal sheet format");
            } catch (IOException e) {
                log.warn("Error parsing google sheet tsv: {}", tsvContet, e);
                throw new RuntimeException(e);
            }
        return validClients;
    }

    private boolean isValid(ClientRequestDto clientRequestDto) {
        Set<ConstraintViolation<ClientRequestDto>> violations = validator.validate(clientRequestDto);
        if (!violations.isEmpty()) {
            violations.forEach(v -> log.warn("Validation failed: {} -> {}", v.getPropertyPath(), v.getMessage()));
            return false;
        }
        return true;
    }

}
