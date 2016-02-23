package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.networking.io.response.PlanningResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlanningPhase extends Phase {

	@Override
	public void applyResponse(GameResponse response) {
		if (response instanceof PlanningResponse) {
			log.info(response.getPlayerIdentifier() + " Planning");
		} else {
			log.error("Expected Planning; got: "
					+ response.getClass() + " from: "
					+ response.getPlayerIdentifier());
		}
	}
}
