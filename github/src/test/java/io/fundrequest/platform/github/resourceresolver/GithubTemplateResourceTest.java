package io.fundrequest.platform.github.resourceresolver;

import io.fundrequest.platform.github.GithubRawClient;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GithubTemplateResourceTest {

    private GithubTemplateResource resource;
    private String owner = "sbfzsf";
    private String repo = "sgf";
    private String branch = "weq";
    private String location = "sgfb";
    private GithubRawClient githubRawClient;

    @BeforeEach
    void setUp() {
        githubRawClient = mock(GithubRawClient.class);
        resource = new GithubTemplateResource(owner, repo, branch, location, githubRawClient);
    }

    @Test
    void getDescription() {
        assertThat(resource.getDescription()).isEqualTo(owner + "/" + repo + "/" + branch + "/" + location);
    }

    @Test
    void getBaseName() {
        assertThat(resource.getBaseName()).isEqualTo(location);
    }

    @Test
    void exists() {
        when(githubRawClient.getContentsAsRaw(owner, repo, branch, location)).thenReturn("fasgzd");

        final boolean result = resource.exists();

        assertThat(result).isTrue();
    }

    @Test
    void exists_contentsEmpty() {
        when(githubRawClient.getContentsAsRaw(owner, repo, branch, location)).thenReturn("");

        final boolean result = resource.exists();

        assertThat(result).isFalse();
    }

    @Test
    void exists_githubclientException() {
        doThrow(new RuntimeException()).when(githubRawClient).getContentsAsRaw(owner, repo, branch, location);

        final boolean result = resource.exists();

        assertThat(result).isFalse();
    }

    @Test
    void reader() throws IOException {
        final String expected = "fasgzd";
        when(githubRawClient.getContentsAsRaw(owner, repo, branch, location)).thenReturn(expected);

        final Reader result = resource.reader();

        assertThat(IOUtils.toString(result)).isEqualTo(expected);
    }

    @Test
    public void exists_cachesResultForReader() throws IOException {
        final String contents = "fasgzd";
        when(githubRawClient.getContentsAsRaw(owner, repo, branch, location)).thenReturn(contents);

        assertThat(resource.exists()).isTrue();
        assertThat(IOUtils.toString(resource.reader())).isEqualTo(contents);

        verify(githubRawClient, times(1)).getContentsAsRaw(owner, repo, branch, location);
    }

    @Test
    public void reader_cachesResultForExists() throws IOException {
        final String contents = "fasgzd";
        when(githubRawClient.getContentsAsRaw(owner, repo, branch, location)).thenReturn(contents);

        assertThat(IOUtils.toString(resource.reader())).isEqualTo(contents);
        assertThat(resource.exists()).isTrue();

        verify(githubRawClient, times(1)).getContentsAsRaw(owner, repo, branch, location);
    }

    @Test
    void relative() {
        final String relativeLocation = "cxncvx";

        final GithubTemplateResource result = resource.relative(relativeLocation);

        assertThat(result.getOwner()).isEqualTo(owner);
        assertThat(result.getRepo()).isEqualTo(repo);
        assertThat(result.getBranch()).isEqualTo(branch);
        assertThat(result.getLocation()).isEqualTo(relativeLocation);
    }
}