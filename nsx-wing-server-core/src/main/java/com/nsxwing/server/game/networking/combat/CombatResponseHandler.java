package com.nsxwing.server.game.networking.combat;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.state.CombatState;

public interface CombatResponseHandler<T extends GameResponse> {
	CombatState handleResponse(T response, CombatState currentState);
}
