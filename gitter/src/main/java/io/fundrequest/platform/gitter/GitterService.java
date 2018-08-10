package io.fundrequest.platform.gitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.platform.github.GithubRawClient;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.List;

public class GitterService {

    private GithubRawClient githubRawClient;
    private final String branch;
    private final String filePath;
    private ObjectMapper objectMapper;

    public GitterService(final GithubRawClient githubRawClient, final String branch, final String path, final ObjectMapper objectMapper) {
        this.githubRawClient = githubRawClient;
        this.branch = branch;
        this.filePath = path;
        this.objectMapper = objectMapper;
    }

    @Cacheable("gitter_fund_notification_rooms")
    public List<String> listFundedNotificationRooms() {
        try {
            final String roomsRaw = githubRawClient.getContentsAsRaw("FundRequest", "content-management", branch, filePath);
            final GitterRooms gitterRooms = objectMapper.readValue(roomsRaw, GitterRooms.class);
            return gitterRooms.getFundedNotification();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
