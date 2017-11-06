package io.fundrequest.restapi.security.civic;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CivicAuthService {

    private CivicAuthClient civicAuthClient;

    public CivicAuthService(CivicAuthClient civicAuthClient) {
        this.civicAuthClient = civicAuthClient;
    }

    @Cacheable("civicUserData")
    public String getUserData(String token) {
        return civicAuthClient.getUserData(token);
    }
}
