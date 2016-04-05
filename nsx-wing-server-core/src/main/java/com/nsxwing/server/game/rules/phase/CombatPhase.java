package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.gameplay.meta.combat.Target;
import com.nsxwing.common.networking.io.event.AttackEvent;
import com.nsxwing.common.networking.io.event.GameEvent;
import com.nsxwing.common.networking.io.event.ModifyAttackEvent;
import com.nsxwing.common.networking.io.event.ModifyEvadeEvent;
import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.state.CombatState;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.nsxwing.common.player.agent.PlayerAgent.COMBAT_ORDER_COMPARATOR;

@Slf4j
public class CombatPhase extends Phase {

	private CombatState currentCombatState;

	public CombatPhase(GameServer gameServer) {
		super(gameServer);
	}

	@Override
	protected synchronized GameState handleResponse(GameResponse response) {
		if (response instanceof AttackResponse) {
			handleAttackResponse((AttackResponse) response);
		} else if (response instanceof ModifyAttackResponse) {
			handleModifyAttackResponse((ModifyAttackResponse) response);
		} else if (response instanceof ModifyEvadeResponse) {
			handleModifyDefenseResponse((ModifyEvadeResponse) response);
		}

		return currentGameState;
	}

	private void handleModifyDefenseResponse(ModifyEvadeResponse response) {
		response.getDiceModifiers().stream()
				.forEach(diceModifer -> diceModifer.modify(currentCombatState.getEvadeDice()));
	}

	private void handleModifyAttackResponse(ModifyAttackResponse response) {
		response.getDiceModifiers().stream()
				.forEach(diceModifer -> diceModifer.modify(currentCombatState.getAttackDice()));
	}

	private void handleAttackResponse(AttackResponse response) {
		currentCombatState.setDefender(response.getTarget());
	}

	@Override
	public GameState playPhase(GameState gameState) {
		currentGameState = gameState;
		currentCombatState = initCombatState(currentGameState);

		currentGameState.getPlayerAgents().stream()
				.sorted(COMBAT_ORDER_COMPARATOR)
				.forEach(this::activateAgent);

		return currentGameState;
	}

	private CombatState initCombatState(GameState gameState) {
		CombatState state = new CombatState();
		state.setChamp(gameState.getChamp());
		state.setScrub(gameState.getScrub());
		return state;
	}

	private void activateAgent(PlayerAgent playerAgent) {
		startAttack(playerAgent);
		chooseTarget(playerAgent);

		currentCombatState.rollAttack();

		modifyDice(currentCombatState.getDefender().getTargetAgent(), buildModifyAttackEvent());
		modifyDice(currentCombatState.getAttacker(), buildModifyAttackEvent());

		currentCombatState.rollEvade();

		modifyDice(currentCombatState.getAttacker(), buildModifyEvadeEvent());
		modifyDice(currentCombatState.getDefender().getTargetAgent(), buildModifyEvadeEvent());

		resolveDamage();
	}

	private void resolveDamage() {
		currentGameState.applyCombat(currentCombatState);
	}

	private ModifyEvadeEvent buildModifyEvadeEvent() {
		return new ModifyEvadeEvent(currentCombatState.getAttacker(),
				currentCombatState.getDefender(),
				currentCombatState.getAttackDice(),
				currentCombatState.getEvadeDice());
	}

	private void modifyDice(PlayerAgent agent, GameEvent event) {
		Player player = prepareEvent(agent);

		gameServer.sendToClient(player.getConnection(), event);

		waitForResponses();
	}

	private ModifyAttackEvent buildModifyAttackEvent() {
		return new ModifyAttackEvent(currentCombatState.getAttacker(),
				currentCombatState.getDefender(),
				currentCombatState.getAttackDice());
	}

	private void waitForResponses() {
		while (!finished()) {
			threadSleeper.accept(50);
		}
	}

	private void startAttack(PlayerAgent playerAgent) {
		currentCombatState.setAttacker(playerAgent);
		currentCombatState.setDefender(null);
	}

	private void chooseTarget(PlayerAgent playerAgent) {
		Player player = prepareEvent(playerAgent);

		List<Target> targets = determineTargets(playerAgent, currentGameState);

		gameServer.sendToClient(player.getConnection(), new AttackEvent(playerAgent, targets));

		waitForResponses();
	}

	private Player prepareEvent(PlayerAgent playerAgent) {
		PlayerIdentifier identifier = playerAgent.getOwner();
		Player player = currentGameState.getPlayerFor(identifier);
		prepareResponseHandler(identifier);
		return player;
	}

	private List<Target> determineTargets(PlayerAgent playerAgent, GameState gameState) {
		return gameState.findTargetsFor(playerAgent);
	}
}
