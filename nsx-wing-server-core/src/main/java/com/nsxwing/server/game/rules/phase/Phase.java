package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public abstract class Phase {

	protected boolean handledChamp;
	protected boolean handledScrub;
	protected GameServer gameServer;
	protected Consumer<Integer> threadSleeper = this::sleep;
	protected GameState currentGameState;

	public Phase(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	public boolean finished() {
		return handledChamp && handledScrub;
	}

	public synchronized void applyResponse(GameResponse response) {
		if (response.getPlayerIdentifier() == PlayerIdentifier.CHAMP) {
			log.info("Handling Champ response.");
			handledChamp = true;
		} else {
			log.info("Handling Scrub response.");
			handledScrub = true;
		}

		handleResponse(response);
	}

	protected abstract GameState handleResponse(GameResponse response);

	public GameState startPhase(GameState gameState) {
		handledChamp = false;
		handledScrub = false;
		return playPhase(gameState);
	}

//	protected void resetResponseHandlers() {
//		handledChamp = false;
//		handledScrub = false;
//	}

	public abstract GameState playPhase(GameState gameState);

	@SneakyThrows
	private void sleep(long millis) {
		Thread.sleep(millis);
	}
}
