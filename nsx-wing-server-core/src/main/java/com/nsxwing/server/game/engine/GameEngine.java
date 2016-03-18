package com.nsxwing.server.game.engine;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.state.GameState;
import com.nsxwing.common.state.GameStateFactory;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PhaseList;
import lombok.Setter;

public class GameEngine {

	private final GameServer gameServer;
	private final PhaseList phaseList;
	private final GameStateFactory gameStateFactory;

	@Setter private Player champ;
	@Setter private Player scrub;

	public GameEngine(GameServer gameServer, PhaseList phaseList, GameStateFactory gameStateFactory) {
		this.gameServer = gameServer;
		this.phaseList = phaseList;
		this.gameStateFactory = gameStateFactory;
	}

	public synchronized void handleResponse(GameResponse response) {
		phaseList.getCurrentPhase().applyResponse(response);
	}

	public GameState playTurn(GameState gameState) {
		GameState latestGameState = gameState;

		for (Phase phase : phaseList.getPhases()) {
			latestGameState = phase.playPhase(latestGameState);
			phaseList.incrementPhase();
		}

		return gameStateFactory.incrementTurn(gameState);
	}
}
