package io.fundrequest.platform.profile.ref.infrastructure.mav;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.JsonWebToken;

import java.io.IOException;

class ArkaneAdviceTest {

    @Test
    public void testDecodeJWT() throws IOException {
        String jwtToken
                = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJmQi1UenBOb0hBVGhwT2J4aW9qTDBrdm83MldmRzRXRXh1eFpiaXlGQUhzIn0.eyJqdGkiOiJhZmZmYjYyZi1iODIxLTQzOGEtOGUyYS0wOTFmOTUwMDkzNzQiLCJleHAiOjE1NDQ2MDU4OTcsIm5iZiI6MCwiaWF0IjoxNTQ0NjA0MDk3LCJpc3MiOiJodHRwczovL2xvZ2luLXN0YWdpbmcuYXJrYW5lLm5ldHdvcmsvYXV0aC9yZWFsbXMvQXJrYW5lIiwiYXVkIjoiRnVuZFJlcXVlc3QiLCJzdWIiOiI2MmZjNzAyOS04MjQ2LTQyMDItYjk0Mi1mZTA1ZGRkZmZhMDciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJGdW5kUmVxdWVzdCIsIm5vbmNlIjoiYTM4M2RiZWQtNWE1ZS00ZThlLTljNmQtNWI4Zjg4MmVjZjFhIiwiYXV0aF90aW1lIjoxNTQ0NjAxMzUwLCJzZXNzaW9uX3N0YXRlIjoiYzVjZTIyMDYtY2YyZS00NzljLWE1ZDMtZjgxNWZjMmRlMzI5IiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL2Z1bmRyZXF1ZXN0LmlvIiwiKiIsImh0dHBzOi8vY29ubmVjdC5hcmthbmUubmV0d29yayJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiXX0sInJlc291cmNlX2FjY2VzcyI6e30sInNjb3BlIjoib3BlbmlkIHZpZXc6d2FsbGV0cyBzaWduOndhbGxldHMgdmlldzpwcm9maWxlIG9mZmxpbmVfYWNjZXNzIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IkRhdnkgVmFuIFJveSIsInByZWZlcnJlZF91c2VybmFtZSI6ImRhdnkudmFuLnJveUBnbWFpbC5jb20iLCJnaXZlbl9uYW1lIjoiRGF2eSBWYW4iLCJmYW1pbHlfbmFtZSI6IlJveSIsImVtYWlsIjoiZGF2eS52YW4ucm95QGdtYWlsLmNvbSJ9.krHSdBd140-WGKy7zGUkFFUILbJQkkMTU87OxtXIozhoFNHl07r_NoJblPC-4YYz6fVFIyrHmAeUWFSWiwXSVeWyr_VMigmITAJREeGAeiZY9QMzkrk2SDsX1ANS3ZKu3p9mZy5c5BFbUXl-RakF2o420ynCug1xxNoKMUZgh5XzAUXUAN9Mkj2CnpP8qcriyNYIhQtvqmT0gWTRrFUvmSbYzHpeoRqjh9mF6OXxF9-nv95ZlokqW0v15rSxnRISTs6hiWKjB1QgnJ2M7edvNnJI4yguYBjhxLYEe6fE-Fwfqe7a7uuYDzArvsgKjC9xweuOsur0iL_iLuRUt-2Y8g\n";
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonWebToken jsonWebToken = objectMapper.readValue(body, JsonWebToken.class);
        System.out.println("JWT Body : " + body);
    }

}