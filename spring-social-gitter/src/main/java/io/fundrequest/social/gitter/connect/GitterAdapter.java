package io.fundrequest.social.gitter.connect;

import io.fundrequest.social.gitter.api.Gitter;
import io.fundrequest.social.gitter.api.Me;
import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;


public class GitterAdapter implements ApiAdapter<Gitter> {

	public boolean test(final Gitter gitter) {
		try {
            Me me = gitter.userResource().me();
            return me != null;
		} catch (ApiException e) {
			return false;
		}
	}

    @Override
    public void setConnectionValues(final Gitter gitter, final ConnectionValues values) {
        final Me me = gitter.userResource().me();
        values.setProviderUserId(me.getId());
        values.setDisplayName(me.getDisplayName());
        values.setProfileUrl(me.getUrl());
        values.setImageUrl(me.getAvatarUrl());
    }

    @Override
    public UserProfile fetchUserProfile(Gitter gitter) {
        final Me me = gitter.userResource().me();
        return new UserProfileBuilder().setId(me.getId()).setUsername(me.getUsername()).setName(me.getDisplayName()).build();
    }

    @Override
    public void updateStatus(Gitter gitter, String message) {
        //Intentionally blank
    }
}
