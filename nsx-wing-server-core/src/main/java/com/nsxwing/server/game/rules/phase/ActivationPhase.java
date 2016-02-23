package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.ActionResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivationPhase extends Phase {

	@Override
	public void applyResponse(GameResponse response) {
		if (response instanceof ActionResponse) {
			log.info(response.getPlayerIdentifier() + " Activating");
		} else {
			log.error("Expected Activation; got: "
					+ response.getClass() + " from: "
					+ response.getPlayerIdentifier());
		}
	}
}
