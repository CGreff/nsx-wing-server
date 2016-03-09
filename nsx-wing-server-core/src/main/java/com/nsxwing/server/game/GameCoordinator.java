package com.nsxwing.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.server.game.networking.GameServer;
import lombok.Getter;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static com.nsxwing.common.player.PlayerIdentifier.SCRUB;

public class GameCoordinator {

	private final GameEngine gameEngine;
	private final GameServer server;

	@Getter
	private Player champ;

	@Getter
	private Player scrub;

	public GameCoordinator(GameServer server, GameEngine gameEngine) {
		this.server = server;
		this.gameEngine = gameEngine;
	}

	public boolean isGameReady() {
		return champ != null && scrub != null;
	}

	public void connectPlayer(Connection playerConnection, ConnectionEvent connectionEvent) {
		if (champ == null) {
			champ = handleConnection(playerConnection, connectionEvent, CHAMP);
			gameEngine.setChamp(champ);
		} else {
			scrub = handleConnection(playerConnection, connectionEvent, SCRUB);
			gameEngine.setScrub(scrub);
		}
	}

	private Player handleConnection(Connection playerConnection, ConnectionEvent connectionEvent, PlayerIdentifier identifier) {
		ConnectionResponse response = new ConnectionResponse();
		response.setPlayerIdentifier(identifier);
		server.sendToClient(playerConnection, response);
		return new Player(identifier, playerConnection, connectionEvent.getPlayerAgents());
	}

	public void handleResponse(GameResponse response) {
		gameEngine.handleResponse(response);
	}
}
