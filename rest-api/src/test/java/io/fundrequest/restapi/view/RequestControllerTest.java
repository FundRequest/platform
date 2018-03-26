package io.fundrequest.restapi.view;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.CanClaimRequest;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.restapi.request.RequestController;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class RequestControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
    private RequestService requestService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Principal principal;


    @Before
    public void setUp() throws Exception {
        requestService = Mockito.mock(RequestService.class);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("davyvanroy@fundrequest.io");
        mockMvc = MockMvcBuilders.standaloneSetup(new RequestController(requestService))
                .setMessageConverters(converter)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void findAll() throws Exception {

        when(requestService.findAll()).thenReturn(Collections.singletonList(RequestDtoMother.freeCodeCampNoUserStories()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/public/requests").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("requests-list-example"));
    }

    @Test
    public void findById() throws Exception {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        when(requestService.findRequest(request.getId())).thenReturn(request);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/public/requests/{id}", request.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("request-get-example"));
    }

    @Test
    public void findRequestsForUser() throws Exception {
        when(requestService.findRequestsForUser(principal)).thenReturn(Collections.singletonList(RequestDtoMother.freeCodeCampNoUserStories()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/public/user/requests").accept(MediaType.APPLICATION_JSON).principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("requests-user-list-example"));
    }


    @Test
    public void addWatcher() throws Exception {

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/private/requests/123/watchers").contentType(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("requests-add-watcher-example"));
    }

    @Test
    public void removeWatcher() throws Exception {

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/private/requests/123/watchers").contentType(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("requests-remove-watcher-example"));
    }

    @Test
    public void claim() throws Exception {
        UserClaimRequest userClaimRequest = UserClaimRequest.builder().platform(Platform.GITHUB).platformId("1").address("0x0").build();
        SignedClaim expected = new SignedClaim("davyvanroy", "0x0", Platform.GITHUB, "1", "r", "s", 1);
        when(requestService.signClaimRequest(principal, userClaimRequest)).thenReturn(expected);
        this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/private/requests/123/claim")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(userClaimRequest))
                        .principal(principal))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(expected)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("requests-claim-example"));
    }

    @Test
    public void canClaim() throws Exception {
        CanClaimRequest canClaimRequest = new CanClaimRequest();
        canClaimRequest.setPlatformId("1");
        canClaimRequest.setPlatform(Platform.GITHUB);
        when(requestService.canClaim(principal, canClaimRequest)).thenReturn(true);
        this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/private/requests/123/can-claim").accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("platform", Platform.GITHUB.toString())
                        .param("platformId", "1")
                        .principal(principal))
                .andExpect(content().string("true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("requests-can-claim-example"));
    }

}