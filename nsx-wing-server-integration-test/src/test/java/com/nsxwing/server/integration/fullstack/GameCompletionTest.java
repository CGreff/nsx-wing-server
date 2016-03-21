package com.nsxwing.server.integration.fullstack;

import com.nsxwing.common.state.GameState;
import com.nsxwing.server.integration.ContextInitializer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GameCompletionTest extends ContextInitializer {

	@Test
	public void shouldBeAbleToPlayToTheEndOfAGame() {
		GameState endGameState = gameCoordinator.playGame();

		assertThat(endGameState, is(not(nullValue())));
		assertTrue(endGameState.isGameComplete());
	}
}
