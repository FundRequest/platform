package io.fundrequest.core.request.infrastructure.github.parser;

public final class GithubResultMother {

    public static GithubResultBuilder kazuki43zooApiStub42() {
        return GithubResultBuilder.aGithubResult()
                .withNumber("42")
                .withState("open")
                .withTitle("Change to use Kotlin instead of Groovy on Mapper Interface");
    }
}
