package com.nsxwing.server.game;

import com.nsxwing.server.game.agent.Player;
import com.nsxwing.server.game.networking.GameServer;

public class GameEngine {

	private final GameServer gameServer;
	private final Player champ;
	private final Player scrub;

	public GameEngine(GameServer gameServer, Player champ, Player scrub) {
		this.gameServer = gameServer;
		this.champ = champ;
		this.scrub = scrub;
	}
}
