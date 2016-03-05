package com.nsxwing.server.main;

import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.server.config.AppContext;
import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.game.networking.GameServer;
import lombok.extern.slf4j.Slf4j;

import static com.nsxwing.server.config.AppContext.getGameCoordinator;
import static com.nsxwing.server.config.AppContext.getGameServer;
import static com.nsxwing.server.config.AppContext.initGameServer;
import static com.nsxwing.server.config.AppContext.getPhaseEngine;

@Slf4j
public class App {

	public static void main(String[] args) {
		PhaseEngine phaseEngine = getPhaseEngine();
		GameServer gameServer = getGameServer();
		GameCoordinator coordinator = getGameCoordinator(gameServer);
		initGameServer(gameServer, phaseEngine, coordinator);

		while(true) {
			gameServer.broadcastEvent(new ActionEvent());
		}
	}
}
