package com.nsxwing.server.game.engine;

import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PlanningPhase;

public class PhaseEngine {

	public Phase getCurrentPhase() {
		return new PlanningPhase();
	}
}
