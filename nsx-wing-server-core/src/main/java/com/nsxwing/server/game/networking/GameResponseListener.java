package com.nsxwing.server.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.engine.PhaseEngine;

public class GameResponseListener extends Listener {

	private PhaseEngine phaseEngine;
	private GameCoordinator gameCoordinator;

	public GameResponseListener(PhaseEngine phaseEngine, GameCoordinator gameCoordinator) {
		this.phaseEngine = phaseEngine;
		this.gameCoordinator = gameCoordinator;
	}

	@Override
	public void received (Connection connection, Object object) {
		if (object instanceof GameResponse) {
			phaseEngine.handleResponse((GameResponse) object);
		} else if (object instanceof ConnectionEvent) {
			gameCoordinator.connectPlayer(connection);
		}
	}
}
