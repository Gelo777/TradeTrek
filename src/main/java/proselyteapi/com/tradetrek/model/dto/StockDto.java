package proselyteapi.com.tradetrek.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDto {

    private Long id;
    private Double price;
    private Long timestamp;
}
