package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.gameplay.meta.combat.Target;
import com.nsxwing.common.networking.io.event.AttackEvent;
import com.nsxwing.common.networking.io.event.GameEvent;
import com.nsxwing.common.networking.io.event.ModifyAttackEvent;
import com.nsxwing.common.networking.io.event.ModifyEvadeEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.state.CombatState;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.combat.CombatResponseHandler;
import com.nsxwing.server.game.networking.GameServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.nsxwing.common.player.agent.PlayerAgent.COMBAT_ORDER_COMPARATOR;

@Slf4j
public class CombatPhase extends Phase {

	private CombatState currentCombatState;
	private Map<Class, CombatResponseHandler> responseHandlers;

	public CombatPhase(GameServer gameServer, Map<Class, CombatResponseHandler> responseHandlers) {
		super(gameServer);
		this.responseHandlers = responseHandlers;
	}

	@Override
	protected synchronized GameState handleResponse(GameResponse response) {
		currentCombatState = responseHandlers.get(response.getClass()).handleResponse(response, currentCombatState);

		return currentGameState;
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
		sendForPlayerAgent(agent, event);
		waitForResponses();
	}

	private ModifyAttackEvent buildModifyAttackEvent() {
		return new ModifyAttackEvent(currentCombatState.getAttacker(),
				currentCombatState.getDefender(),
				currentCombatState.getAttackDice());
	}

	private void startAttack(PlayerAgent playerAgent) {
		currentCombatState = initCombatState(currentGameState);
		currentCombatState.setAttacker(playerAgent);
		currentCombatState.setDefender(null);
	}

	private void chooseTarget(PlayerAgent playerAgent) {
		List<Target> targets = currentGameState.findTargetsFor(playerAgent);

		sendForPlayerAgent(playerAgent, new AttackEvent(playerAgent, targets));

		waitForResponses();
	}
}
