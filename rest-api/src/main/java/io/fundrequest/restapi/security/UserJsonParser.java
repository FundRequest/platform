package io.fundrequest.restapi.security;

import com.jayway.jsonpath.JsonPath;
import io.fundrequest.core.user.UserLoginCommand;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class UserJsonParser {

    public UserLoginCommand parseUserLoginFromJson(String token) {
        String email = (String) ((JSONArray) JsonPath.parse(token).read("$.data.[?(@.label == 'contact.personal.email')].value")).get(0);
        String phoneNo = (String) ((JSONArray) JsonPath.parse(token).read("$.data.[?(@.label == 'contact.personal.phoneNumber')].value")).get(0);
        String userId = JsonPath.parse(token).read("$.userId", String.class);
        return new UserLoginCommand(userId, phoneNo, email);
    }
}
