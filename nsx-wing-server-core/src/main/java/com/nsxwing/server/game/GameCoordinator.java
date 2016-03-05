package com.nsxwing.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.server.game.agent.Player;
import com.nsxwing.server.game.networking.GameServer;
import java.util.Optional;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static com.nsxwing.common.player.PlayerIdentifier.SCRUB;

public class GameCoordinator {

	GameServer server;
	Player champ;
	Player scrub;

	public GameCoordinator(GameServer server) {
		this.server = server;
	}

	public Optional<GameEngine> fetchGameEngine() {
		if (champ != null && scrub != null) {
			return Optional.of(new GameEngine(server, champ, scrub));
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
}
