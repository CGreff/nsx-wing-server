package com.nsxwing.server.networking.engine;

import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.networking.GameServer;

public class EventEngine {

	private PhaseEngine phaseEngine;
	private GameServer gameServer;

	public EventEngine(GameServer gameServer, PhaseEngine phaseEngine) {
		this.phaseEngine = phaseEngine;
	}
}
