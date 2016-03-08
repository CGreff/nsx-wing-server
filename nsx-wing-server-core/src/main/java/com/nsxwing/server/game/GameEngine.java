package com.nsxwing.server.game;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.game.networking.GameServer;
import lombok.Setter;

public class GameEngine {

	private final GameServer gameServer;
	private final PhaseEngine phaseEngine;

	@Setter private Player champ;
	@Setter private Player scrub;

	public GameEngine(GameServer gameServer, PhaseEngine phaseEngine) {
		this.gameServer = gameServer;
		this.phaseEngine = phaseEngine;
	}

	public void handleResponse(GameResponse response) {
		phaseEngine.handleResponse(response);
	}
}
