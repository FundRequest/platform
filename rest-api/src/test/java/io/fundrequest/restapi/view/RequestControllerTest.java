package io.fundrequest.restapi.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.fundrequest.core.request.RequestService;
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

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/public/requests").accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcRestDocumentation.document("requests-list-example"));
    }

    @Test
    public void findById() throws Exception {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        Mockito.when(requestService.findRequest(request.getId())).thenReturn(request);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/public/requests/{id}", request.getId()).accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcRestDocumentation.document("request-get-example"));
    }

    @Test
    public void findRequestsForUser() throws Exception {
        Mockito.when(requestService.findRequestsForUser(principal)).thenReturn(Collections.singletonList(RequestDtoMother.freeCodeCampNoUserStories()));

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

}