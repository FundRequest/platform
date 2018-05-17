package io.fundrequest.platform.profile.survey.domain;

import io.fundrequest.platform.profile.survey.infrastructue.SurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository repository;

    public SurveyServiceImpl(SurveyRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public SurveyDto getSurveyResult(Principal principal) {
        return repository.findByUserId(principal.getName())
                         .map(s -> SurveyDto.builder()
                                            .status(s.getStatus())
                                            .transactionHash(s.getTransactionHash())
                                            .build()
                             ).orElse(null);
    }


}
