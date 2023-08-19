package proselyteapi.com.tradetrek.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proselyteapi.com.tradetrek.model.dto.CompanyDto;
import proselyteapi.com.tradetrek.service.CompanyService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class CompaniesController {

    @Autowired
    private  CompanyService companyService;

    @GetMapping("/companies")
    public Flux<ResponseEntity<CompanyDto>> getCompanies() {
        return companyService.getAllCompanies().map(ResponseEntity::ok);
    }
}
