package com.nsxwing.server.game.networking.combat;

import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.state.CombatState;

public class AttackResponseHandler implements CombatResponseHandler<AttackResponse> {
	@Override
	public CombatState handleResponse(AttackResponse response, CombatState currentState) {
		currentState.setDefender(response.getTarget());
		return currentState;
	}
}
