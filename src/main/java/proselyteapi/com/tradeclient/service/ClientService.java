package proselyteapi.com.tradeclient.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import proselyteapi.com.tradeclient.model.ClientStock;
import proselyteapi.com.tradeclient.repository.ClientStockRepository;
import proselyteapi.com.tradetrek.model.dto.CompanyDto;
import proselyteapi.com.tradetrek.model.dto.StockDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor

public class ClientService {

    @Value("${company-url}")
    private String companyUrl;

    @Value("${stock-url}")
    private String stockUrl;

    private final WebClient webClient = WebClient.builder().build();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ClientStockRepository clientStockRepository;

    @PostConstruct
    public void init() {
        fetchAndProcessStockData();
    }


    public void fetchAndProcessStockData() {
        fetchCompanies().flatMap(companyDto -> fetchStock(companyDto.getSymbol())
                        .flatMap(stockDto -> clientStockRepository.findByCompanyName(companyDto.getName())
                                .flatMap(existingClientStock -> {

                                    existingClientStock.setOldPrice(existingClientStock.getNewPrice());
                                    existingClientStock.setNewPrice(stockDto.getPrice());

                                    return clientStockRepository.save(existingClientStock);
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    ClientStock newClientStock = new ClientStock();
                                    newClientStock.setCompanyName(companyDto.getName());
                                    newClientStock.setOldPrice(stockDto.getPrice());
                                    newClientStock.setNewPrice(stockDto.getPrice());

                                    return clientStockRepository.save(newClientStock);
                                }))
                                .then(Mono.just(stockDto)))
                        .subscribeOn(Schedulers.fromExecutor(executorService)))
                .subscribe();

        executorService.shutdown();
    }

    public Mono<StockDto> fetchStock(String stockCode) {
        return webClient.get()
                .uri(stockUrl, stockCode)
                .retrieve()
                .bodyToMono(StockDto.class);
    }

    public Flux<CompanyDto> fetchCompanies() {
        return webClient.get()
                .uri(companyUrl)
                .retrieve()
                .bodyToFlux(CompanyDto.class);
    }
}
