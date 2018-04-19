package io.fundrequest.platform.profile.survey.domain;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.profile.survey.infrastructue.SurveyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SurveyServiceImpl implements SurveyService, ApplicationListener<AuthenticationSuccessEvent> {

    private final SurveyRepository repository;
    private String spreadsheetId;
    private String googleClientSecret;
    private static final String SHEET_RANGE = "A2:D";
    private KeycloakRepository keycloakRepository;

    public SurveyServiceImpl(SurveyRepository repository, @Value("${io.fundrequest.tokensale.status.spreadsheet-id}") String spreadsheetId,
                             @Value("${io.fundrequest.tokensale.status.google-sheets-client-secret}") String googleClientSecret, KeycloakRepository keycloakRepository) {
        this.repository = repository;
        this.spreadsheetId = spreadsheetId;
        this.googleClientSecret = googleClientSecret;
        this.keycloakRepository = keycloakRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public SurveyDto getSurveyResult(Principal principal) {
        return repository.findByUserId(principal.getName())
                         .map(s -> SurveyDto.builder()
                                            .status(s.getStatus())
                                            .build()
                             ).orElse(null);
    }

    @Transactional
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        try {
            IDToken idToken = ((KeycloakAuthenticationToken) event.getAuthentication()).getAccount().getKeycloakSecurityContext().getIdToken();
            repository.findByEmail(idToken.getEmail().toLowerCase()).ifPresent(s -> {
                if (StringUtils.isBlank(s.getUserId())) {
                    String userId = event.getAuthentication().getName();
                    s.setUserId(userId);
                    if (StringUtils.isNotBlank(keycloakRepository.getEtherAddress(userId))) {
                        keycloakRepository.updateEtherAddress(userId, s.getEtherAddress());
                    }
                    repository.save(s);
                }
            });
        } catch (Exception e) {
            log.error("An exception ocurred when checking survey", e);
        }
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void loadSurveys() {
        final ValueRange response;
        try {
            response = getSheetsService().spreadsheets().values()
                                         .get(spreadsheetId, SHEET_RANGE)
                                         .execute();
            final List<List<Object>> values = response.getValues();
            Set<Survey> surveysToSave = new HashSet<>();
            values.forEach(v -> {
                String fieldValue = getRowValue(v, 3);
                if (StringUtils.isNotBlank(fieldValue)) {
                    String email = fieldValue.toLowerCase();
                    if (!repository.findByEmail(email).isPresent()) {
                        Survey survey = Survey.builder()
                                              .email(email.toLowerCase().trim())
                                              .etherAddress(getEtherAddress(v))
                                              .status(SurveyStatus.UNVERIFIED)
                                              .build();
                        surveysToSave.add(survey);
                    }
                }
            });
            repository.save(surveysToSave);
        } catch (Exception e) {
            log.error("Error when going to google sheets", e);
        }
    }

    private String getEtherAddress(List<Object> v) {
        String rowValue = getRowValue(v, 0);
        return rowValue == null || rowValue.length() > 99 ? null : rowValue;
    }

    private String getRowValue(final List row, final int i) {
        return i < row.size() ? row.get(i).toString().trim() : null;
    }

    private Sheets getSheetsService() throws Exception {
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), authorize())
                .setApplicationName("TOKENSALE-STATUS")
                .build();
    }

    private Credential authorize() throws IOException {
        final List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS);
        return GoogleCredential
                .fromStream(new ByteArrayInputStream(this.googleClientSecret.getBytes()))
                .createScoped(scopes);
    }


}
