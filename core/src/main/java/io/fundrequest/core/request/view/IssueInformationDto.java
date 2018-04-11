package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.Platform;

public class IssueInformationDto {

    private String owner;
    private String repo;
    private String number;
    private String title;
    private Platform platform;
    private String platformId;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getUrl() {
        if (platform == Platform.GITHUB) {
            return "https://github.com/" + owner + "/" + repo + "/issues/" + number;
        }
        return "";
    }

}
