package proselyteapi.com.tradetrek.model.dto;

import lombok.Getter;
import lombok.Setter;
import proselyteapi.com.tradetrek.model.entity.Stock;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class CompanyDto {
    private Long id;
    private String symbol;
    private String name;
    private Mono<Stock> stock;
}
