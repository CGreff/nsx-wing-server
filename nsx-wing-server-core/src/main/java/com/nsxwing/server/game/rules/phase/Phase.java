package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.GameResponse;

public abstract class Phase {

	protected boolean handledChamp;
	protected boolean handledScrub;

	public boolean finished() {
		return handledChamp && handledScrub;
	}

	public void applyResponse(GameResponse response) {

	}
}
