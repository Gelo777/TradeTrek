package proselyteapi.com.tradetrek.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proselyteapi.com.tradetrek.model.dto.CompanyDto;
import proselyteapi.com.tradetrek.model.exception.EntityNotFoundException;
import proselyteapi.com.tradetrek.model.mapper.CompanyMapper;
import proselyteapi.com.tradetrek.repository.CompanyRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public Flux<CompanyDto> getAllCompanies() {
        return companyRepository.findAll()
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Список компаний пустой")))
                .map(companyMapper::toCompanyDto);
    }
}
