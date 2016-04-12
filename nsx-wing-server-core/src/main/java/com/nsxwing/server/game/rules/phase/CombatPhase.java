package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.gameplay.meta.combat.Target;
import com.nsxwing.common.networking.io.event.AttackEvent;
import com.nsxwing.common.networking.io.event.ModifyAttackEvent;
import com.nsxwing.common.networking.io.event.ModifyEvadeEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.state.CombatState;
import com.nsxwing.common.state.CombatStateFactory;
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
	private CombatStateFactory combatStateFactory;
	private Map<Class, CombatResponseHandler> responseHandlers;

	public CombatPhase(GameServer gameServer, CombatStateFactory combatStateFactory, Map<Class, CombatResponseHandler> responseHandlers) {
		super(gameServer);
		this.combatStateFactory = combatStateFactory;
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

		currentGameState.getPlayerAgents().stream()
				.sorted(COMBAT_ORDER_COMPARATOR)
				.forEach(this::activateAgent);

		return currentGameState;
	}

	private void activateAgent(PlayerAgent playerAgent) {
		startAttack(playerAgent);
		chooseTarget(playerAgent);

		handleAttackDice();

		handleEvadeDice();

		currentGameState.applyCombat(currentCombatState);
	}

	private void handleEvadeDice() {
		currentCombatState.rollEvade();

		sendForPlayerAgent(currentCombatState.getAttacker(), buildModifyEvadeEvent());
		sendForPlayerAgent(currentCombatState.getDefender().getTargetAgent(), buildModifyEvadeEvent());

	}

	private void handleAttackDice() {
		currentCombatState.rollAttack();

		sendForPlayerAgent(currentCombatState.getDefender().getTargetAgent(), buildModifyAttackEvent());
		sendForPlayerAgent(currentCombatState.getAttacker(), buildModifyAttackEvent());

	}

	private ModifyEvadeEvent buildModifyEvadeEvent() {
		return new ModifyEvadeEvent(currentCombatState.getAttacker(),
				currentCombatState.getDefender(),
				currentCombatState.getAttackDice(),
				currentCombatState.getEvadeDice());
	}

	private ModifyAttackEvent buildModifyAttackEvent() {
		return new ModifyAttackEvent(currentCombatState.getAttacker(),
				currentCombatState.getDefender(),
				currentCombatState.getAttackDice());
	}

	private void startAttack(PlayerAgent playerAgent) {
		currentCombatState = combatStateFactory.buildInitialCombatState(currentGameState);
		currentCombatState.setAttacker(playerAgent);
		currentCombatState.setDefender(null);
	}

	private void chooseTarget(PlayerAgent playerAgent) {
		List<Target> targets = currentGameState.findTargetsFor(playerAgent);

		sendForPlayerAgent(playerAgent, new AttackEvent(playerAgent, targets));
	}
}
