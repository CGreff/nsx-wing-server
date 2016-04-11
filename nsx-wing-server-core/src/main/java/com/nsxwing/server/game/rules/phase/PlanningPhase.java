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

		Map<String, Maneuver> maneuvers = combineManeuvers(currentGameState.getPlannedManeuvers(), planningResponse.getAgentManeuvers());

		currentGameState = new GameState(
				currentGameState.getChamp(),
				currentGameState.getScrub(),
				currentGameState.getPlayerAgents(),
				maneuvers,
				currentGameState.getTurnNumber());

		return currentGameState;
	}

	private Map<String, Maneuver> combineManeuvers(Map<String, Maneuver> currentManeuvers, Map<String, Maneuver> agentManeuvers) {
		Map<String, Maneuver> combinedManeuverMap = new HashMap<>();
		combinedManeuverMap.putAll(currentManeuvers);
		combinedManeuverMap.putAll(agentManeuvers);
		return combinedManeuverMap;
	}

	@Override
	@SneakyThrows
	public GameState playPhase(GameState gameState) {
		currentGameState = gameState;
		currentGameState.setPlannedManeuvers(new HashMap<>());

		sendPlanningEvent();

		waitForResponses();

		return currentGameState;
	}

	private void sendPlanningEvent() {
		PlanningEvent event = new PlanningEvent();
		event.setGameState(currentGameState);
		gameServer.broadcastEvent(event);
	}
}
