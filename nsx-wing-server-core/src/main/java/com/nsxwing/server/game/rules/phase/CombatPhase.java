package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CombatPhase extends Phase {

	public CombatPhase(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	public GameState handleResponse(GameResponse response) {
		if (response instanceof AttackResponse) {
			log.info(response.getPlayerIdentifier() + " Attacking");
		} else {
			log.error("Expected Attack; got: "
					+ response.getClass() + " from: "
					+ response.getPlayerIdentifier());
		}
		return null;
	}

	@Override
	public GameState playPhase(GameState gameState) {
		return gameState;
	}
}
