package proselyteapi.com.tradetrek.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proselyteapi.com.tradetrek.model.dto.CompanyDto;
import proselyteapi.com.tradetrek.model.mapper.CompanyMapper;
import proselyteapi.com.tradetrek.repository.CompanyRepository;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public Flux<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().map(companyMapper::toCompanyDto);
    }
}