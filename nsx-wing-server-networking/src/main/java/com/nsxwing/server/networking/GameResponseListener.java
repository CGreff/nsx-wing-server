package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.server.game.engine.PhaseEngine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameResponseListener extends Listener {

	private PhaseEngine phaseEngine;

	public GameResponseListener(PhaseEngine phaseEngine) {
		this.phaseEngine = phaseEngine;
	}

	@Override
	public void received (Connection connection, Object object) {
		if (object instanceof GameResponse) {
			phaseEngine.handleResponse((GameResponse) object);
		} else {
			throw new IllegalArgumentException("Server expected Game Response but got: " + object);
		}
	}

}
