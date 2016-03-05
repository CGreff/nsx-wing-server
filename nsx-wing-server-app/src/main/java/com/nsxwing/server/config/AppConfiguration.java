package com.nsxwing.server.config;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.game.rules.phase.ActivationPhase;
import com.nsxwing.server.game.rules.phase.CombatPhase;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PlanningPhase;
import com.nsxwing.server.networking.GameResponseListener;
import com.nsxwing.server.networking.GameServer;
import lombok.SneakyThrows;

import java.util.List;

import static java.util.Arrays.asList;

public class AppConfiguration {

	protected static List<Phase> getPhases() {
		return asList(new PlanningPhase(), new ActivationPhase(), new CombatPhase());
	}

	public static PhaseEngine getPhaseEngine() {
		return new PhaseEngine(getPhases());
	}

	@SneakyThrows
	public static GameServer initGameServer(PhaseEngine phaseEngine) {
		GameServer server = new GameServer(new Server(), new GameResponseListener(phaseEngine));
		server.start();
		return server;
	}
}
