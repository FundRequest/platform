package io.fundrequest.core.request.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.RequestService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
    private RequestService requestService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Principal principal;


    @Before
    public void setUp() throws Exception {
        requestService = mock(RequestService.class);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        converter.setObjectMapper(objectMapper);
        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("davyvanroy@fundrequest.io");
        mockMvc = MockMvcBuilders.standaloneSetup(new RequestController(requestService))
                .setMessageConverters(converter)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void findAll() throws Exception {

        when(requestService.findAll()).thenReturn(Collections.singletonList(RequestOverviewDtoMother.freeCodeCampNoUserStories()));

        this.mockMvc.perform(get("/requests").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("requests-list-example"));
    }

    @Test
    public void findById() throws Exception {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        when(requestService.findRequest(request.getId())).thenReturn(request);

        this.mockMvc.perform(get("/requests/{id}", request.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("request-get-example"));
    }

    @Test
    public void findRequestsForUser() throws Exception {
        when(requestService.findRequestsForUser(principal)).thenReturn(Collections.singletonList(RequestOverviewDtoMother.freeCodeCampNoUserStories()));

        this.mockMvc.perform(get("/user/requests").accept(MediaType.APPLICATION_JSON).principal(principal))
                .andExpect(status().isOk())
                .andDo(document("requests-user-list-example"));
    }

    @Test
    public void createRequest() throws Exception {
        CreateRequestCommand command = new CreateRequestCommand();
        command.setIssueLink("https://github.com/freeCodeCamp/freeCodeCamp/issues/14258");
        command.setTechnologies(Collections.singleton("java"));

        when(requestService.createRequest(principal, command))
                .thenReturn(RequestOverviewDtoMother.freeCodeCampNoUserStories());

        this.mockMvc.perform(
                post("/requests").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(command))
                        .principal(principal))
                .andExpect(
                        status().isCreated())
                .andDo(document("requests-create-example",
                        requestFields(
                                fieldWithPath("issueLink").description("The Github link to the issue"),
                                fieldWithPath("technologies").description("An array of technologies")
                        )));
    }

    @Test
    public void addWatcher() throws Exception {

        this.mockMvc.perform(
                post("/requests/123/watchers").contentType(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(
                        status().isCreated())
                .andDo(document("requests-add-watcher-example"));
    }

    @Test
    public void removeWatcher() throws Exception {

        this.mockMvc.perform(
                delete("/requests/123/watchers").contentType(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(
                        status().isOk())
                .andDo(document("requests-remove-watcher-example"));
    }

    @Test
    public void badRequest() throws Exception {

        this.mockMvc.perform(
                post("/requests").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(Collections.emptyMap()))
                        .principal(principal))
                .andExpect(
                        status().isBadRequest())
                .andDo(document("requests-create-error-example"));
    }
}