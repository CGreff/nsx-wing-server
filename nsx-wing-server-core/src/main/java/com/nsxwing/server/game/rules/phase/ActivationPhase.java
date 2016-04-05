package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.common.networking.io.response.ActionResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.position.Maneuver;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static com.nsxwing.common.player.agent.PlayerAgent.ACTIVATION_ORDER_COMPARATOR;

@Slf4j
public class ActivationPhase extends Phase {

	private Map<String, Maneuver> plannedManeuvers;
	Function<GameState, GameState> gameStateManeuverStripper = this::cloneGameStateWithoutManeuvers;

	public ActivationPhase(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	protected synchronized GameState handleResponse(GameResponse response) {
		ActionResponse actionResponse = (ActionResponse) response;

		currentGameState = actionResponse.getAction().execute(currentGameState);
		return currentGameState;
	}

	@Override
	@SneakyThrows
	public GameState playPhase(GameState gameState) {
		plannedManeuvers = gameState.getPlannedManeuvers();
		currentGameState = gameStateManeuverStripper.apply(gameState);

		currentGameState.getPlayerAgents().stream()
				.sorted(ACTIVATION_ORDER_COMPARATOR)
				.forEach(this::activateAgent);

		return currentGameState;
	}

	protected GameState cloneGameStateWithoutManeuvers(GameState gameState) {
		return new GameState(
				gameState.getChamp(),
				gameState.getScrub(),
				gameState.getPlayerAgents(),
				new HashMap<>(),
				gameState.getTurnNumber());
	}

	private void activateAgent(PlayerAgent playerAgent) {
		PlayerIdentifier identifier = playerAgent.getOwner();
		Player player = currentGameState.getPlayerFor(identifier);
		prepareResponseHandler(identifier);
		String agentId = playerAgent.getAgentId();
		currentGameState.maneuverAgent(agentId, plannedManeuvers.get(agentId));

		gameServer.sendToClient(player.getConnection(), new ActionEvent(currentGameState));

		while (!finished()) {
			threadSleeper.accept(50);
		}
	}
}
