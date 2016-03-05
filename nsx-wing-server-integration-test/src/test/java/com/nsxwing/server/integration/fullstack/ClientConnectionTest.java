package com.nsxwing.server.integration.fullstack;

import com.nsxwing.server.integration.ContextInitializer;
import org.junit.Test;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static com.nsxwing.common.player.PlayerIdentifier.SCRUB;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ClientConnectionTest extends ContextInitializer {

	@Test
	public void shouldHavePlayerObjectsWhoKnowWhoTheyAre() {
		//Because the game is started in the setup
		//we should see that clients have received their player identifier.
		assertThat(champClient.getPlayerIdentifier(), is(CHAMP));
		assertThat(scrubClient.getPlayerIdentifier(), is(SCRUB));
	}
}
