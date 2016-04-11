package com.nsxwing.server.game.networking.combat;

import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
import com.nsxwing.common.state.CombatState;

public class ModifyAttackResponseHandler implements CombatResponseHandler<ModifyAttackResponse> {
	@Override
	public CombatState handleResponse(ModifyAttackResponse response, CombatState currentState) {
		response.getDiceModifiers().stream()
				.forEach(diceModifer -> diceModifer.modify(currentState.getAttackDice()));

		return currentState;
	}
}
