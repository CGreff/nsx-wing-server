package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.event.PlanningEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.networking.io.response.PlanningResponse;
import com.nsxwing.common.position.Maneuver;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PlanningPhase extends Phase {

	public PlanningPhase(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	protected synchronized GameState handleResponse(GameResponse response) {
		PlanningResponse planningResponse = (PlanningResponse) response;

		Map<String, Maneuver> combinedManeuverMap = new HashMap<>();
		combinedManeuverMap.putAll(currentGameState.getPlannedManeuvers());
		combinedManeuverMap.putAll(planningResponse.getAgentManeuvers());

		currentGameState = new GameState(
				currentGameState.getChamp(),
				currentGameState.getScrub(),
				currentGameState.getPlayerAgents(),
				combinedManeuverMap,
				currentGameState.getTurnNumber());

		return currentGameState;
	}

	@Override
	@SneakyThrows
	public GameState playPhase(GameState gameState) {
		currentGameState = gameState;
		currentGameState.setPlannedManeuvers(new HashMap<>());

		PlanningEvent event = new PlanningEvent();
		event.setGameState(currentGameState);

		gameServer.broadcastEvent(event);

		while (!finished()) {
			threadSleeper.accept(50);
		}

		return currentGameState;
	}
}
