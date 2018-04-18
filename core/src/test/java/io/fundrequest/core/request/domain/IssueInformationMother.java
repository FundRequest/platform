package io.fundrequest.core.request.domain;

public final class IssueInformationMother {

    public static IssueInformationBuilder kazuki43zooApiStub() {
        return IssueInformationBuilder.anIssueInformation()
                                      .withLink("https://github.com/kazuki43zoo/api-stub/issues/42")
                                      .withNumber("42")
                                      .withOwner("kazuki43zoo")
                                      .withRepo("api-stub")
                                      .withPlatform(Platform.GITHUB)
                                      .withPlatformId("kazuki43zoo|FR|api-stub|FR|42")
                                      .withTitle("Change to use Kotlin instead of Groovy on Mapper Interface");
    }

    public static IssueInformationBuilder fundRequestArea51() {
        return IssueInformationBuilder.anIssueInformation()
                                      .withLink("https://github.com/FundRequest/area51/issues/52")
                                      .withNumber("52")
                                      .withOwner("FundRequest")
                                      .withRepo("area51")
                                      .withPlatform(Platform.GITHUB)
                                      .withPlatformId("FundRequest|FR|area51|FR|52")
                                      .withTitle("add hello world for python");
    }
}
