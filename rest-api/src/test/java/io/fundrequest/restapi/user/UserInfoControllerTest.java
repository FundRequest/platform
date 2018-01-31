package io.fundrequest.restapi.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.fundrequest.core.user.UserAuthentication;
import io.fundrequest.core.user.UserDtoMother;
import io.fundrequest.core.user.UserService;
import io.fundrequest.core.user.dto.UserDto;
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

public class UserInfoControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
    private UserService userService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Principal principal;


    @Before
    public void setUp() throws Exception {
        userService = Mockito.mock(UserService.class);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        converter.setObjectMapper(objectMapper);
        UserDto davy = UserDtoMother.davy();
        principal = new UserAuthentication(davy.getUserId(), davy.getEmail());
        mockMvc = MockMvcBuilders.standaloneSetup(new UserInfoController(userService))
                .setMessageConverters(converter)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void findAll() throws Exception {
        UserDto user = UserDtoMother.davy();

        Mockito.when(userService.getUser(user.getUserId())).thenReturn(user);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/private/user/info").accept(MediaType.APPLICATION_JSON).principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("user-info-example"));
    }

}