package com.nsxwing.server.config;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.state.GameStateFactory;
import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.engine.GameEngine;
import com.nsxwing.server.game.networking.GameResponseListener;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.rules.phase.PhaseList;
import lombok.SneakyThrows;

public final class AppContext {

	private AppContext() {}

	public static GameServer getGameServer() {
		return new GameServer(new Server());
	}

	public static GameEngine getGameEngine(final GameServer gameServer) {
		return  new GameEngine(gameServer, new PhaseList(), new GameStateFactory());
	}

	public static GameCoordinator getGameCoordinator(final GameServer server, final GameEngine gameEngine) {
		return new GameCoordinator(server, gameEngine, new GameStateFactory());
	}

	@SneakyThrows
	public static void initGameServer(final GameServer server, final GameCoordinator coordinator) {
		server.start(new GameResponseListener(coordinator));
	}


}
