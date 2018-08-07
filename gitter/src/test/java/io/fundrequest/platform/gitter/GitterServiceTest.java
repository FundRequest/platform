package io.fundrequest.platform.gitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.platform.github.GithubRawClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GitterServiceTest {

    private GitterService gitterService;
    private GithubRawClient githubRawClient;
    private String branch;
    private String filePath;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        githubRawClient = mock(GithubRawClient.class);
        branch = "branch";
        filePath = "filePath";
        objectMapper = mock(ObjectMapper.class);
        gitterService = new GitterService(githubRawClient, branch, filePath, objectMapper);
    }

    @Test
    void listFundedNotificationChannels() throws IOException {
        final String json = "json";
        final List<String> expectedChannels = Arrays.asList("FundRequest/funded-requests-test", "FundRequest/funded-requests", "FundRequest/funded-requests-blablabla");
        final GitterRooms gitterRooms = new GitterRooms();
        gitterRooms.setFundedNotification(expectedChannels);

        when(githubRawClient.getContentsAsRaw("FundRequest", "content-management", branch, filePath)).thenReturn(json);
        when(objectMapper.readValue(json, GitterRooms.class)).thenReturn(gitterRooms);

        final List<String> result = gitterService.listFundedNotificationRooms();

        assertThat(result).isEqualTo(expectedChannels);
    }
}
