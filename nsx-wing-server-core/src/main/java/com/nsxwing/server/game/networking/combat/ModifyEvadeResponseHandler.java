package com.nsxwing.server.game.networking.combat;

import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
import com.nsxwing.common.state.CombatState;

public class ModifyEvadeResponseHandler implements CombatResponseHandler<ModifyEvadeResponse> {
	@Override
	public CombatState handleResponse(ModifyEvadeResponse response, CombatState currentState) {
		response.getDiceModifiers().stream()
				.forEach(diceModifer -> diceModifer.modify(currentState.getEvadeDice()));

		return  currentState;
	}
}
