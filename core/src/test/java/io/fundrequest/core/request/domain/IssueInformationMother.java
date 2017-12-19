package io.fundrequest.core.request.domain;

public final class IssueInformationMother {

    public static IssueInformationBuilder kazuki43zooApiStub() {
        return IssueInformationBuilder.anIssueInformation()
                .withLink("https://github.com/kazuki43zoo/api-stub/issues/42")
                .withNumber("42")
                .withOwner("kazuki43zoo")
                .withRepo("api-stub")
                .withPlatform(Platform.GITHUB)
                .withPlatformId("3")
                .withTitle("Change to use Kotlin instead of Groovy on Mapper Interface");
    }
}
