package com.nsxwing.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.server.game.networking.GameServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameCoordinatorTest {

	@InjectMocks
	private GameCoordinator underTest;

	@Mock
	private GameServer server;

	@Mock
	private GameEngine gameEngine;

	@Mock
	private Connection connection;

	@Mock
	private ConnectionEvent connectionEvent;

	@Mock
	private PlayerAgent playerAgent;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		doReturn(singletonList(playerAgent)).when(connectionEvent).getPlayerAgents();
	}

	@Test
	public void shouldNotBeReadyIfNoPlayersHaveConnected() {
		boolean result = underTest.isGameReady();
		assertFalse(result);
	}

	@Test
	public void shouldNotBeReadyIfOnlyOnePlayerConnected() {
		underTest.connectPlayer(connection, connectionEvent);
		boolean result = underTest.isGameReady();

		assertFalse(result);
	}

	@Test
	public void shouldBeReadyWhenBothPlayersConnected() {
		underTest.connectPlayer(connection, connectionEvent);
		underTest.connectPlayer(connection, connectionEvent);
		boolean result = underTest.isGameReady();

		assertTrue(result);
	}

	@Test
	public void shouldSendAResponseToAClientWhenConnected() {
		underTest.connectPlayer(connection, connectionEvent);

		verify(server).sendToClient(eq(connection), any(ConnectionResponse.class));
	}

	@Test
	public void shouldAssignPlayerToGameEngineOnConnection() {
		//Test both players connect
		underTest.connectPlayer(connection, connectionEvent);
		underTest.connectPlayer(connection, connectionEvent);

		verify(gameEngine).setChamp(any(Player.class));
		verify(gameEngine).setScrub(any(Player.class));
	}

	@Test
	public void shouldPassResponsesOffToPhaseEngine() {
		GameResponse response = mock(GameResponse.class);
		underTest.handleResponse(response);

		verify(gameEngine).handleResponse(response);
	}

	@Test
	public void shouldAssignPlayerAgentsOnConnection() {
		underTest.connectPlayer(connection, connectionEvent);

		assertThat(underTest.getChamp().getPlayerAgents(), hasItem(playerAgent));
	}
}