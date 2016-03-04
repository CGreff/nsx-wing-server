package com.nsxwing.server.main;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.game.rules.phase.ActivationPhase;
import com.nsxwing.server.game.rules.phase.CombatPhase;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PlanningPhase;
import com.nsxwing.server.networking.GameResponseListener;
import com.nsxwing.server.networking.GameServer;
import com.nsxwing.server.networking.engine.EventEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
public class App {

	public static void main(String[] args) {
		//Still better than Spring.
		List<Phase> phases = asList(new ActivationPhase(), new CombatPhase(), new PlanningPhase());
		PhaseEngine phaseEngine = new PhaseEngine(phases);

		GameServer gameServer = new GameServer(new Server(), new GameResponseListener(phaseEngine));

		EventEngine eventEngine = new EventEngine(gameServer, phaseEngine);

		while (true) {
			gameServer.broadcastEvent(new ActionEvent());
		}

	}

}
