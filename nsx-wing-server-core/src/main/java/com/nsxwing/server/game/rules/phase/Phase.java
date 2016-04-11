package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.event.GameEvent;
import com.nsxwing.common.networking.io.event.GameplayEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;

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

	public abstract GameState playPhase(GameState gameState);

	@SneakyThrows
	private void sleep(long millis) {
		Thread.sleep(millis);
	}

	protected void prepareResponseHandler(PlayerIdentifier identifier) {
		handledChamp = identifier != CHAMP;
		handledScrub = !handledChamp;
	}

	protected void waitForResponses() {
		while (!finished()) {
			threadSleeper.accept(50);
		}
	}

	protected void sendForPlayerAgent(PlayerAgent playerAgent, GameEvent event) {
		Player player = currentGameState.getPlayerFor(playerAgent);
		prepareResponseHandler(player.getIdentifier());
		gameServer.sendToClient(player.getConnection(), event);
		waitForResponses();
	}
}
