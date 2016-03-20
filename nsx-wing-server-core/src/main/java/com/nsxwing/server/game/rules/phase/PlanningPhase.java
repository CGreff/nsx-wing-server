package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.event.PlanningEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.networking.io.response.PlanningResponse;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.position.Maneuver;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

@Slf4j
public class PlanningPhase extends Phase {

	private GameState currentGameState;

	public PlanningPhase(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public synchronized void handleResponse(GameResponse response) {
		PlanningResponse planningResponse = (PlanningResponse) response;

		Map<Integer, Maneuver> combinedManeuverMap = new HashMap<>();
		combinedManeuverMap.putAll(currentGameState.getPlannedManeuvers());
		combinedManeuverMap.putAll(planningResponse.getAgentManeuvers());

		currentGameState = new GameState(
				currentGameState.getPlayerAgents(),
				combinedManeuverMap,
				currentGameState.getTurnNumber());
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
			sleep(50);
		}

		return gameState;
	}
}
