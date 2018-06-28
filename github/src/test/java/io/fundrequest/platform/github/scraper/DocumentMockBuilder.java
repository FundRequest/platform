package io.fundrequest.platform.github.scraper;

import io.fundrequest.platform.github.scraper.model.GithubId;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentMockBuilder {

    public List<Element> discussionItems = new ArrayList<>();

    private DocumentMockBuilder() {
    }

    public static DocumentMockBuilder documentBuilder() {
        return new DocumentMockBuilder();
    }

    public static DiscussionItemBuilder discussionItemBuilder() {
        return DocumentMockBuilder.DiscussionItemBuilder.builder();
    }

    public DocumentMockBuilder addDiscussionItem(final Element discussionItem) {
        discussionItems.add(discussionItem);
        return this;
    }

    public Document build() {
        final Document document = mock(Document.class);
        when(document.select(".discussion-item")).thenReturn(new Elements(discussionItems));
        return document;
    }

    public static class DiscussionItemBuilder {

        private final Element element;

        private DiscussionItemBuilder() {
            this.element = mock(Element.class, RETURNS_DEEP_STUBS);
        }

        public static DiscussionItemBuilder builder() {
            return new DiscussionItemBuilder();
        }

        public DiscussionItemBuilder isPullRequest(boolean isPullRequest) {
            final Elements pullRequestElements = mock(Elements.class);
            when(pullRequestElements.isEmpty()).thenReturn(!isPullRequest);
            when(element.select(".discussion-item [id^=ref-pullrequest-]")).thenReturn(pullRequestElements);
            return this;
        }

        public DiscussionItemBuilder isMerged(boolean isMerged) {
            final Elements mergedElements = mock(Elements.class);
            when(mergedElements.isEmpty()).thenReturn(!isMerged);
            when(element.select(".discussion-item span[title=State: merged]")).thenReturn(mergedElements);
            return this;
        }

        public DiscussionItemBuilder withAuthor(final String author) {
            final Elements authorElement = mock(Elements.class);
            when(authorElement.text()).thenReturn(author);
            when(element.select(".discussion-item a.author")).thenReturn(authorElement);
            return this;
        }

        public DiscussionItemBuilder withPullrequestReference(final GithubId githubId, final boolean isInlinePullRequest) {
            final Elements pullRequestElements = mock(Elements.class);

            when(pullRequestElements.isEmpty()).thenReturn(!isInlinePullRequest);
            when(element.select(".discussion-item .discussion-item-rollup-ref [id^=ref-pullrequest-]")).thenReturn(pullRequestElements);

            final String githubIssueId = String.format("/%s/%s/pulls/%s", githubId.getOwner(), githubId.getRepo(), githubId.getNumber());
            if (isInlinePullRequest) {
                when(element.select(".discussion-item [id^=ref-pullrequest-] a").attr("href")).thenReturn(githubIssueId);
            } else {
                when(element.select(".discussion-item [id^=ref-pullrequest-] ~ .discussion-item-ref-title a").attr("href")).thenReturn(githubIssueId);
            }
            return this;
        }

        public Element build() {
            return element;
        }
    }
}
