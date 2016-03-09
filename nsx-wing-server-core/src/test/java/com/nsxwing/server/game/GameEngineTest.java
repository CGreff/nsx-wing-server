package com.nsxwing.server.game;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.server.game.networking.GameServer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameEngineTest {

	@InjectMocks
	private GameEngine underTest;

	@Mock
	private GameServer gameServer;

	@Mock
	private Player champ;

	@Mock
	private Player scrub;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		underTest.setChamp(champ);
		underTest.setScrub(scrub);
	}

	@Test
	@Ignore
	public void shouldSendGameResponsesToThePhaseEngine() {
		GameResponse mockResponse = mock(GameResponse.class);
		underTest.handleResponse(mockResponse);

		verifyZeroInteractions(gameServer);
	}

	@Test
	public void shouldReturnCurrentTurnNumber() {
		assertThat(underTest.getCurrentTurnNumber(), is(0));
	}

	@Test
	public void shouldIncrementCurrentTurnNumberWhenATurnIsPlayed() {
		assertThat(underTest.getCurrentTurnNumber(), is(0));

		underTest.playTurn();

		assertThat(underTest.getCurrentTurnNumber(), is(1));
	}
}