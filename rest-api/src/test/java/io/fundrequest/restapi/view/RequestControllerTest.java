package io.fundrequest.restapi.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.command.CreateRequestCommand;
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
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Collections;

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
        converter.setObjectMapper(objectMapper);
        principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("davyvanroy@fundrequest.io");
        mockMvc = MockMvcBuilders.standaloneSetup(new RequestController(requestService))
                                 .setMessageConverters(converter)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
                                 .build();
    }

    @Test
    public void findAll() throws Exception {

        Mockito.when(requestService.findAll()).thenReturn(Collections.singletonList(RequestDtoMother.freeCodeCampNoUserStories()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/private/requests").accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcRestDocumentation.document("requests-list-example"));
    }

    @Test
    public void findById() throws Exception {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        Mockito.when(requestService.findRequest(request.getId())).thenReturn(request);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/private/requests/{id}", request.getId()).accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcRestDocumentation.document("request-get-example"));
    }

    @Test
    public void findRequestsForUser() throws Exception {
        Mockito.when(requestService.findRequestsForUser(principal)).thenReturn(Collections.singletonList(RequestDtoMother.freeCodeCampNoUserStories()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/private/user/requests").accept(MediaType.APPLICATION_JSON).principal(principal))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcRestDocumentation.document("requests-user-list-example"));
    }

    @Test
    public void createRequest() throws Exception {
        CreateRequestCommand command = new CreateRequestCommand();
        command.setIssueLink("https://github.com/FundRequest/area51/issues/4");
        command.setTechnologies(Collections.singleton("java"));

        Mockito.when(requestService.createRequest(principal, command))
               .thenReturn(RequestDtoMother.freeCodeCampNoUserStories());

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/private/requests").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(command))
                                                .principal(principal))
                    .andExpect(
                            MockMvcResultMatchers.status().isCreated())
                    .andDo(MockMvcRestDocumentation.document("requests-create-example",
                                                             PayloadDocumentation.requestFields(
                                                                     PayloadDocumentation.fieldWithPath("issueLink").description("The Github link to the issue"),
                                                                     PayloadDocumentation.fieldWithPath("technologies").description("An array of technologies")
                                                                                               )));
    }

    @Test
    public void addWatcher() throws Exception {

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/private/requests/123/watchers").contentType(MediaType.APPLICATION_JSON)
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
    public void badRequest() throws Exception {

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/private/requests").contentType(MediaType.APPLICATION_JSON).content(
                        this.objectMapper.writeValueAsString(Collections.emptyMap()))
                                                .principal(principal))
                    .andExpect(
                            MockMvcResultMatchers.status().isBadRequest())
                    .andDo(MockMvcRestDocumentation.document("requests-create-error-example"));
    }
}