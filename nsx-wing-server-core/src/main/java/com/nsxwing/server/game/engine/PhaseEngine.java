package com.nsxwing.server.game.engine;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.server.game.rules.phase.Phase;

import java.util.List;


public class PhaseEngine {

	private final List<Phase> phases;
	private int currentPhaseIndex;

	public PhaseEngine(List<Phase> phases) {
		this.phases = phases;
		this.currentPhaseIndex = 0;
	}

	public Phase getCurrentPhase() {
		return phases.get(currentPhaseIndex);
	}

	public void handleResponse(GameResponse response) {
		phases.get(currentPhaseIndex).applyResponse(response);

		if (phases.get(currentPhaseIndex).finished()) {
			currentPhaseIndex = (currentPhaseIndex + 1) % phases.size();
		}
	}
}
