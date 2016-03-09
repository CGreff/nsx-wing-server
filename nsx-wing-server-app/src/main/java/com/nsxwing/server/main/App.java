package com.nsxwing.server.main;

import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.GameEngine;
import com.nsxwing.server.game.networking.GameServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.nsxwing.server.config.AppContext.getGameCoordinator;
import static com.nsxwing.server.config.AppContext.getGameEngine;
import static com.nsxwing.server.config.AppContext.getGameServer;
import static com.nsxwing.server.config.AppContext.initGameServer;

@Slf4j
public class App {

	@SneakyThrows
	public static void main(String[] args) {
		GameServer gameServer = getGameServer();
		GameEngine gameEngine = getGameEngine(gameServer);
		GameCoordinator coordinator = getGameCoordinator(gameServer, gameEngine);
		initGameServer(gameServer, coordinator);
		log.info("Game Server started. Waiting on Clients.");

		while (!coordinator.isGameReady()) {
			Thread.sleep(500);
		}

		log.info("--- Starting game ---");
//		gameEngine.play();
	}
}
