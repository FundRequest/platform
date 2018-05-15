package io.fundrequest.common.infrastructure;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
public class JsoupSpringWrapper {

    public Document parse(final String html, final String baseUri) {
        return Jsoup.parse(html, baseUri);
    }

    public Document parse(final String html, final String baseUri, final Parser parser) {
        return Jsoup.parse(html, baseUri, parser);
    }

    public Document parse(final String html) {
        return Jsoup.parse(html);
    }

    public Connection connect(final String url) {
        return Jsoup.connect(url);
    }

    public Document parse(final File in, final String charsetName, final String baseUri) throws IOException {
        return Jsoup.parse(in, charsetName, baseUri);
    }

    public Document parse(final File in, final String charsetName) throws IOException {
        return Jsoup.parse(in, charsetName);
    }

    public Document parse(final InputStream in, final String charsetName, final String baseUri) throws IOException {
        return Jsoup.parse(in, charsetName, baseUri);
    }

    public Document parse(final InputStream in, final String charsetName, final String baseUri, final Parser parser) throws IOException {
        return Jsoup.parse(in, charsetName, baseUri, parser);
    }

    public Document parseBodyFragment(final String bodyHtml, final String baseUri) {
        return Jsoup.parseBodyFragment(bodyHtml, baseUri);
    }

    public Document parseBodyFragment(final String bodyHtml) {
        return Jsoup.parseBodyFragment(bodyHtml);
    }

    public Document parse(final URL url, int timeoutMillis) throws IOException {
        return Jsoup.parse(url, timeoutMillis);
    }

    public final String clean(final String bodyHtml, final String baseUri, final Whitelist whitelist) {
        return Jsoup.clean(bodyHtml, baseUri, whitelist);
    }

    public final String clean(final String bodyHtml, final Whitelist whitelist) {
        return Jsoup.clean(bodyHtml, whitelist);
    }

    public final String clean(final String bodyHtml, final String baseUri, final Whitelist whitelist, final Document.OutputSettings outputSettings) {
        return Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
    }

    public boolean isValid(final String bodyHtml, final Whitelist whitelist) {
        return Jsoup.isValid(bodyHtml, whitelist);
    }
}
