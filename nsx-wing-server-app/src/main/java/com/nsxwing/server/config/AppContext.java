package com.nsxwing.server.config;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.engine.PhaseEngine;
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

	public static PhaseEngine getPhaseEngine() {
		return new PhaseEngine(getPhases());
	}

	public static GameServer getGameServer() {
		return new GameServer(new Server());
	}

	public static GameCoordinator getGameCoordinator(GameServer server, PhaseEngine phaseEngine) {
		return new GameCoordinator(server, phaseEngine);
	}

	@SneakyThrows
	public static void initGameServer(GameServer server, GameCoordinator coordinator) {
		server.start(new GameResponseListener(coordinator));
	}


}
