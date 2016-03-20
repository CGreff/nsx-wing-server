package com.nsxwing.server.game.rules.phase;

import com.nsxwing.server.game.networking.GameServer;
import lombok.Getter;

import java.util.List;

import static java.util.Arrays.asList;

public class PhaseList {

	@Getter
	private List<Phase> phases;

	public PhaseList(GameServer gameServer) {
		phases = asList(
				new PlanningPhase(gameServer),
				new ActivationPhase(gameServer),
				new CombatPhase(gameServer));
	}

	private int currentPhaseIndex;

	public Phase getCurrentPhase() {
		return phases.get(currentPhaseIndex);
	}

	public void incrementPhase() {
		currentPhaseIndex = (currentPhaseIndex + 1) % phases.size();
	}
}
