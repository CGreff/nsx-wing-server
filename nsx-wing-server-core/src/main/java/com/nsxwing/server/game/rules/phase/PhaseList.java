package com.nsxwing.server.game.rules.phase;

import lombok.Getter;

import java.util.List;

import static java.util.Arrays.asList;

public class PhaseList {

	@Getter
	private List<Phase> phases = asList(
			new PlanningPhase(),
			new ActivationPhase(),
			new CombatPhase());

	private int currentPhaseIndex;

	public Phase getCurrentPhase() {
		return phases.get(currentPhaseIndex);
	}

	public void incrementPhase() {
		currentPhaseIndex = (currentPhaseIndex + 1) % phases.size();
	}
}
