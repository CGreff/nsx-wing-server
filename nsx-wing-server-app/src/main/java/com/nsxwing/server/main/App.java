package com.nsxwing.server.main;

import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.networking.GameServer;
import lombok.extern.slf4j.Slf4j;

import static com.nsxwing.server.config.AppConfiguration.initGameServer;
import static com.nsxwing.server.config.AppConfiguration.getPhaseEngine;

@Slf4j
public class App {

	public static void main(String[] args) {
		PhaseEngine phaseEngine = getPhaseEngine();
		GameServer gameServer = initGameServer(phaseEngine);

		while(true) {
			gameServer.broadcastEvent(new ActionEvent());
		}
	}
}
