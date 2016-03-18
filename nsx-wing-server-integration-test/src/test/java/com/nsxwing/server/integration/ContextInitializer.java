package com.nsxwing.server.integration;

import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.engine.GameEngine;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.integration.client.MockClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;

import static com.nsxwing.server.config.AppContext.getGameCoordinator;
import static com.nsxwing.server.config.AppContext.getGameEngine;
import static com.nsxwing.server.config.AppContext.getGameServer;
import static com.nsxwing.server.config.AppContext.initGameServer;

/*
 * This class is used to start a game in a test context.
 * All tests in this module should inherit from ContextInitializer.
 * Commonly used classes like the GameEngine or Clients are exposed as protected fields.
 */
@Slf4j
public class ContextInitializer {

	protected GameEngine gameEngine;
	protected GameServer gameServer;
	protected GameCoordinator coordinator;
	protected MockClient champClient;
	protected MockClient scrubClient;

	@Before
	@SneakyThrows
	public void initApp() {
		champClient = new MockClient();
		scrubClient = new MockClient();

		gameServer = getGameServer();
		gameEngine = getGameEngine(gameServer);

		coordinator = getGameCoordinator(gameServer, gameEngine);
		initGameServer(gameServer, coordinator);
		log.info("Game Server started. Waiting on Clients.");

		champClient.connect();
		scrubClient.connect();

		while (!coordinator.isGameReady()) {
			log.info("Checking for client connections.");
			Thread.sleep(100);
		}

		log.info("Got a Game Engine. Running tests.");
	}

	@After
	public void tearDown() {
		gameServer.stop();
	}
}
