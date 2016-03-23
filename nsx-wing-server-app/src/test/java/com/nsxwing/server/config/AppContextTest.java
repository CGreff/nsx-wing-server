package com.nsxwing.server.config;

import com.nsxwing.server.game.coordination.GameCoordinator;
import com.nsxwing.server.game.engine.GameEngine;
import com.nsxwing.server.game.networking.GameServer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AppContextTest {

	@Test
	public void shouldProvideAGameServer() {
		GameServer result = AppContext.getGameServer();

		assertThat(result, instanceOf(GameServer.class));
	}

	@Test
	public void shouldProvideAGameEngine() {
		GameEngine result = AppContext.getGameEngine(mock(GameServer.class));

		assertThat(result, instanceOf(GameEngine.class));
	}

	@Test
	public void shouldProvideAGameCoordinator() {
		GameCoordinator result = AppContext.getGameCoordinator(mock(GameServer.class), mock(GameEngine.class));

		assertThat(result, instanceOf(GameCoordinator.class));
	}

	@Test
	public void shouldStartAGameServer() {
		GameServer server = AppContext.getGameServer();
		AppContext.initGameServer(server, mock(GameCoordinator.class));

		assertTrue(server.isRunning());
	}
}