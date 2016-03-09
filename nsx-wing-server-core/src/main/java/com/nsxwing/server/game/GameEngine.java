package com.nsxwing.server.game;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.server.game.networking.GameServer;
import lombok.Getter;
import lombok.Setter;

public class GameEngine {

	private final GameServer gameServer;

	@Setter private Player champ;
	@Setter private Player scrub;
	@Getter private int currentTurnNumber;

	public GameEngine(GameServer gameServer) {
		this.gameServer = gameServer;
		this.currentTurnNumber = 0;
	}

	public void handleResponse(GameResponse response) {
	}

	public void playTurn() {
		currentTurnNumber++;
	}
}
