package proselyteapi.com.tradetrek.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import proselyteapi.com.tradetrek.model.dto.StockDto;
import proselyteapi.com.tradetrek.model.entity.Company;
import proselyteapi.com.tradetrek.model.mapper.StockMapper;
import proselyteapi.com.tradetrek.repository.CompanyRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockService {

    private final CompanyRepository companyRepository;
    private final StockMapper stockMapper;

    @Cacheable(value = "stockCache")
    public Mono<StockDto> getStockBySymbol(String stockCode) {
        return companyRepository.findBySymbol(stockCode).flatMap(Company::getStock).map(stockMapper::toStockDto);
    }
}