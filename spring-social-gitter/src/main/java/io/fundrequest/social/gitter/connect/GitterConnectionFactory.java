package io.fundrequest.social.gitter.connect;

import io.fundrequest.social.gitter.api.Gitter;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class GitterConnectionFactory extends OAuth2ConnectionFactory<Gitter> {

	public GitterConnectionFactory(final String consumerKey, final String consumerSecret) {
		super("gitter", new GitterServiceProvider(consumerKey, consumerSecret), new GitterAdapter());
	}
	
}