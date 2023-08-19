package proselyteapi.com.tradetrek.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import proselyteapi.com.tradetrek.model.dto.StockDto;
import proselyteapi.com.tradetrek.model.entity.Company;
import proselyteapi.com.tradetrek.model.entity.Stock;
import proselyteapi.com.tradetrek.model.mapper.StockMapper;
import proselyteapi.com.tradetrek.repository.CompanyRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

class StockServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStockBySymbol() {
        Company company = new Company();
        Stock stock = new Stock();
        stock.setPrice(50.0);
        company.setStock(Mono.just(stock));

        when(companyRepository.findBySymbol("AAPL")).thenReturn(Mono.just(company));
        when(stockMapper.toStockDto(stock)).thenReturn(new StockDto());

        Mono<StockDto> stockDtoMono = stockService.getStockBySymbol("AAPL");
        StepVerifier.create(stockDtoMono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
