package com.nsxwing.server.main;

import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.GameEngine;
import com.nsxwing.server.game.networking.GameServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.nsxwing.server.config.AppContext.getGameCoordinator;
import static com.nsxwing.server.config.AppContext.getGameServer;
import static com.nsxwing.server.config.AppContext.getPhaseEngine;
import static com.nsxwing.server.config.AppContext.initGameServer;

@Slf4j
public class App {

	@SneakyThrows
	public static void main(String[] args) {
		GameServer gameServer = getGameServer();
		GameCoordinator coordinator = getGameCoordinator(gameServer, getPhaseEngine());
		initGameServer(gameServer, coordinator);
		log.info("Game Server started. Waiting on Clients.");

		Optional<GameEngine> engineOptional = coordinator.fetchGameEngine();
		while (!engineOptional.isPresent()) {
			Thread.sleep(500);
			engineOptional = coordinator.fetchGameEngine();
		}

		log.info("--- Starting game ---");
		GameEngine gameEngine = engineOptional.get();
	}
}
