package com.nsxwing.server.game.agent;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.player.PlayerIdentifier;

public class Player {

	private PlayerIdentifier identifier;
	private Connection connection;

	public Player(PlayerIdentifier identifier, Connection connection) {
		this.identifier = identifier;
		this.connection = connection;
	}
}
