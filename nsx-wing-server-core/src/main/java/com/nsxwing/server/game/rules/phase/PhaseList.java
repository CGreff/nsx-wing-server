package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.networking.combat.AttackResponseHandler;
import com.nsxwing.server.game.networking.combat.CombatResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyAttackResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyEvadeResponseHandler;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class PhaseList {

	@Getter
	private List<Phase> phases;

	public PhaseList(GameServer gameServer) {
		phases = asList(
				new PlanningPhase(gameServer),
				new ActivationPhase(gameServer),
				new CombatPhase(gameServer, createCombatResponseHandlers()));
	}

	Map<Class, CombatResponseHandler> createCombatResponseHandlers() {
		Map<Class, CombatResponseHandler> responseHandlers = new HashMap<>();
		responseHandlers.put(AttackResponse.class, new AttackResponseHandler());
		responseHandlers.put(ModifyAttackResponse.class, new ModifyAttackResponseHandler());
		responseHandlers.put(ModifyEvadeResponse.class, new ModifyEvadeResponseHandler());
		return responseHandlers;
	}

	private int currentPhaseIndex;

	public Phase getCurrentPhase() {
		return phases.get(currentPhaseIndex);
	}

	public void incrementPhase() {
		currentPhaseIndex = (currentPhaseIndex + 1) % phases.size();
	}
}
