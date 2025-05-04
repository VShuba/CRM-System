package ua.shpp.service;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.shpp.dto.ClientResponseDto;

import java.io.PrintWriter;

@Service
public class ExportClientsService {

    private final ClientService clientService;

    public ExportClientsService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void exportClientsForOrganization(Long orgId, PrintWriter writer) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("id"));
        Page<ClientResponseDto> clientsPage;
        HeaderColumnNameMappingStrategy<ClientResponseDto> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(ClientResponseDto.class);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withSeparator('\t')
                .withMappingStrategy(strategy)
                .build();
        do {
            clientsPage =
                    clientService.getClientsByOrganization(orgId, pageable);
            beanToCsv.write(clientsPage.get().toList().stream());
            pageable = pageable.next();
        } while (clientsPage.hasNext());
        writer.close();

    }

}
