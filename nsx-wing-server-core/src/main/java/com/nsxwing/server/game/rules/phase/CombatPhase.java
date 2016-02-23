package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CombatPhase extends Phase {

	@Override
	public void applyResponse(GameResponse response) {
		if (response instanceof AttackResponse) {
			log.info(response.getPlayerIdentifier() + " Attacking");
		} else {
			log.error("Expected Attack; got: "
					+ response.getClass() + " from: "
					+ response.getPlayerIdentifier());
		}
	}
}
