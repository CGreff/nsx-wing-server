package com.nsxwing.server.config;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.GameEngine;
import com.nsxwing.server.game.networking.GameResponseListener;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.rules.phase.ActivationPhase;
import com.nsxwing.server.game.rules.phase.CombatPhase;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PlanningPhase;
import lombok.SneakyThrows;

import java.util.List;

import static java.util.Arrays.asList;

public class AppContext {

	protected static List<Phase> getPhases() {
		return asList(new PlanningPhase(), new ActivationPhase(), new CombatPhase());
	}

	public static GameServer getGameServer() {
		return new GameServer(new Server());
	}

	public static GameEngine getGameEngine(GameServer gameServer) {
		return  new GameEngine(gameServer);
	}

	public static GameCoordinator getGameCoordinator(GameServer server, GameEngine gameEngine) {
		return new GameCoordinator(server, gameEngine);
	}

	@SneakyThrows
	public static void initGameServer(GameServer server, GameCoordinator coordinator) {
		server.start(new GameResponseListener(coordinator));
	}


}
