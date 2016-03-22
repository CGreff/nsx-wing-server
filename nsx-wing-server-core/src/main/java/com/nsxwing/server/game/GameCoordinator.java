package com.nsxwing.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.state.GameState;
import com.nsxwing.common.state.GameStateFactory;
import com.nsxwing.server.game.engine.GameEngine;
import com.nsxwing.server.game.networking.GameServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static com.nsxwing.common.player.PlayerIdentifier.SCRUB;

@Slf4j
public class GameCoordinator {

	private final GameEngine gameEngine;
	private final GameServer server;
	private final GameStateFactory gameStateFactory;

	@Getter
	private Player champ;

	@Getter
	private Player scrub;

	public GameCoordinator(GameServer server, GameEngine gameEngine, GameStateFactory gameStateFactory) {
		this.server = server;
		this.gameEngine = gameEngine;
		this.gameStateFactory = gameStateFactory;
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
		int connectionId = playerConnection.getID();

		server.sendToClient(connectionId, response);

		return new Player(identifier, connectionId, connectionEvent.getPlayerAgents());
	}

	public void handleResponse(GameResponse response) {
		gameEngine.handleResponse(response);
	}

	public GameState playGame() {
		GameState gameState = gameStateFactory.buildInitialGameState(champ, scrub);

		while (!gameState.isGameComplete()) {
			log.info("Playing turn: " + gameState.getTurnNumber());
			gameState = gameEngine.playTurn(gameState);
		}

		return gameState;
	}
}
