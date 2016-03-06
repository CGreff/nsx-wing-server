package com.nsxwing.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.server.game.agent.Player;
import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.game.networking.GameServer;

import java.util.Optional;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static com.nsxwing.common.player.PlayerIdentifier.SCRUB;

public class GameCoordinator {

	private final PhaseEngine phaseEngine;
	private GameEngine gameEngine;
	private GameServer server;
	private Player champ;
	private Player scrub;

	public GameCoordinator(GameServer server, PhaseEngine phaseEngine) {
		this.server = server;
		this.phaseEngine = phaseEngine;
	}

	public Optional<GameEngine> fetchGameEngine() {
		if (champ != null && scrub != null) {
			gameEngine = new GameEngine(server, champ, scrub);
			return Optional.of(gameEngine);
		}
		return Optional.empty();
	}

	public void connectPlayer(Connection playerConnection) {
		if (champ == null) {
			champ = handleConnection(playerConnection, CHAMP);
		} else {
			scrub = handleConnection(playerConnection, SCRUB);
		}
	}

	private Player handleConnection(Connection playerConnection, PlayerIdentifier identifier) {
		ConnectionResponse response = new ConnectionResponse();
		response.setPlayerIdentifier(identifier);
		server.sendToClient(playerConnection, response);
		return new Player(identifier, playerConnection);
	}

	public void handleResponse(GameResponse response) {
		phaseEngine.handleResponse(response);
	}
}
