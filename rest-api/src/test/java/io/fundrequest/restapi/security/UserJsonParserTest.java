package io.fundrequest.restapi.security;

import org.junit.Before;
import org.junit.Test;

public class UserJsonParserTest {

    private UserJsonParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new UserJsonParser();
    }

    @Test
    public void parseUserFromJson() throws Exception {
        parser.parseUserLoginFromJson("{\"data\":[{\"label\":\"contact.personal.email\",\"value\":\"info@fundrequest.io\",\"isValid\":true,\"isOwner\":true},{\"label\":\"contact.personal.phoneNumber\",\"value\":\"+32 472871144\",\"isValid\":true,\"isOwner\":true}],\"userId\":\"6929168ef798dc9025217b653eb1b50c57ff7e340311c644c7b28cdf16caa141\"}");
    }
}