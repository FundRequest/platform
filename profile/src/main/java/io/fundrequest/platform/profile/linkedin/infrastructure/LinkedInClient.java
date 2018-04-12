package io.fundrequest.platform.profile.linkedin.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInShare;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInUpdateResult;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LinkedInClient {

    private final ObjectMapper objectMapper;

    public LinkedInClient() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public LinkedInUpdateResult postNetworkUpdate(String accessToken, LinkedInShare content) {
        String url = "https://api.linkedin.com/v1/people/~/shares?format=json";
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        addAuthorizationHeader(accessToken, httpPost);
        httpPost.addHeader("Content-Type", "application/json");
        try {
            String json = objectMapper.writeValueAsString(content);
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            HttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("An error occurred while posting a LinkedIn network update");
            }
            return objectMapper.readValue(EntityUtils.toString(response.getEntity()), LinkedInUpdateResult.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LinkedInUser getUserInfo(String accessToken) {
        String url = "https://api.linkedin.com/v1/people/~?format=json";
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        addAuthorizationHeader(accessToken, httpGet);
        try {
            HttpResponse response = httpclient.execute(httpGet);
            return objectMapper.readValue(EntityUtils.toString(response.getEntity()), LinkedInUser.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addAuthorizationHeader(String accessToken, HttpMessage httpMessage) {
        httpMessage.addHeader("Authorization", "Bearer " + accessToken);
    }
}
