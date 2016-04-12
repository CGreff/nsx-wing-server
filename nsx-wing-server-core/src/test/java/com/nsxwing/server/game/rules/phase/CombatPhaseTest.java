package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.gameplay.meta.combat.Target;
import com.nsxwing.common.networking.io.event.AttackEvent;
import com.nsxwing.common.networking.io.event.ModifyAttackEvent;
import com.nsxwing.common.networking.io.event.ModifyEvadeEvent;
import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.state.CombatState;
import com.nsxwing.common.state.CombatStateFactory;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.networking.combat.AttackResponseHandler;
import com.nsxwing.server.game.networking.combat.CombatResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyAttackResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyEvadeResponseHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static com.nsxwing.common.player.PlayerIdentifier.SCRUB;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class CombatPhaseTest {
	private CombatPhase underTest;

	@Mock
	private GameServer gameServer;

	@Mock
	private CombatStateFactory combatStateFactory;

	@Mock
	private Map<Class, CombatResponseHandler> responseHandlers;

	@Mock
	private GameState gameState;

	@Mock
	private CombatState combatState;

	@Mock
	private Player champ;

	@Mock
	private Player scrub;

	@Mock
	private PlayerAgent attacker;

	@Mock
	private Target target;

	@Mock
	private PlayerAgent defender;

	@Mock
	private AttackResponseHandler attackResponseHandler;

	@Mock
	private ModifyAttackResponseHandler modifyAttackResponseHandler;

	@Mock
	private ModifyEvadeResponseHandler modifyEvadeResponseHandler;

	private List<PlayerAgent> agents;

	private int attackerConnectionId = 1;
	private int targetConnectionId = 2;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		//This is used to test the asynchronous nature of waiting for events
		setupResponseHandlers();

		underTest = new CombatPhase(gameServer, combatStateFactory, responseHandlers);
		underTest.threadSleeper = this::handleSleep;

		doReturn(combatState).when(combatStateFactory).buildInitialCombatState(gameState);
		mockPlayers();
		mockGameState();
	}

	private void mockPlayers() {
		agents = asList(attacker);
		doReturn(CHAMP).when(attacker).getOwner();
		doReturn(SCRUB).when(defender).getOwner();
		doReturn(CHAMP).when(champ).getIdentifier();
		doReturn(CHAMP).when(scrub).getIdentifier();
		doReturn(attackerConnectionId).when(champ).getConnection();
		doReturn(targetConnectionId).when(scrub).getConnection();
		doReturn(defender).when(target).getTargetAgent();
	}

	private void mockGameState() {
		doReturn(agents).when(gameState).getPlayerAgents();
		doReturn(champ).when(gameState).getPlayerFor(attacker);
		doReturn(scrub).when(gameState).getPlayerFor(defender);
		doReturn(target).when(combatState).getDefender();
		doReturn(attacker).when(combatState).getAttacker();
	}

	private void setupResponseHandlers() {
		responseHandlers = new HashMap<>();

		doReturn(combatState).when(attackResponseHandler).handleResponse(any(AttackResponse.class), any(CombatState.class));
		doReturn(combatState).when(modifyAttackResponseHandler).handleResponse(any(ModifyAttackResponse.class), any(CombatState.class));
		doReturn(combatState).when(modifyEvadeResponseHandler).handleResponse(any(ModifyEvadeResponse.class), any(CombatState.class));

		responseHandlers.put(AttackResponse.class, attackResponseHandler);
		responseHandlers.put(ModifyAttackResponse.class, modifyAttackResponseHandler);
		responseHandlers.put(ModifyEvadeResponse.class, modifyEvadeResponseHandler);
	}

	private void handleSleep(long millis) {
		underTest.handledScrub = true;
		underTest.handledChamp = true;
	}

	@Test
	public void shouldCallTheAttackResponseHandler() {
		AttackResponse response = new AttackResponse();

		underTest.handleResponse(response);

		verify(attackResponseHandler).handleResponse(eq(response), any(CombatState.class));
	}

	@Test
	public void shouldCallTheModifyAttackResponseHandler() {
		ModifyAttackResponse response = new ModifyAttackResponse();

		underTest.handleResponse(response);

		verify(modifyAttackResponseHandler).handleResponse(eq(response), any(CombatState.class));
	}

	@Test
	public void shouldCallTheModifyEvadeResponseHandler() {
		ModifyEvadeResponse response = new ModifyEvadeResponse();

		underTest.handleResponse(response);

		verify(modifyEvadeResponseHandler).handleResponse(eq(response), any(CombatState.class));
	}

	@Test
	public void shouldSendAnAttackEventToTheAgentsConnection() {
		underTest.playPhase(gameState);

		verify(gameServer).sendToClient(eq(attackerConnectionId), isA(AttackEvent.class));
	}

	@Test
	public void shouldRollAttackDice() {
		underTest.playPhase(gameState);

		verify(combatState).rollAttack();
	}

	@Test
	public void shouldAskDefenderForAttackDiceInputBeforeAttacker() {
		InOrder inOrder = inOrder(gameServer);

		underTest.playPhase(gameState);

		inOrder.verify(gameServer).sendToClient(eq(targetConnectionId), isA(ModifyAttackEvent.class));
		inOrder.verify(gameServer).sendToClient(eq(attackerConnectionId), isA(ModifyAttackEvent.class));
	}

	@Test
	public void shouldRollEvadeDiceAfterTheAttack() {
		InOrder inOrder = inOrder(combatState);

		underTest.playPhase(gameState);

		inOrder.verify(combatState).rollAttack();
		inOrder.verify(combatState).rollEvade();
	}

	@Test
	public void shouldAskAttackerForEvadeDiceInputBeforeDefender() {
		InOrder inOrder = inOrder(gameServer);

		underTest.playPhase(gameState);

		inOrder.verify(gameServer).sendToClient(eq(attackerConnectionId), isA(ModifyEvadeEvent.class));
		inOrder.verify(gameServer).sendToClient(eq(targetConnectionId), isA(ModifyEvadeEvent.class));
	}

	@Test
	public void shouldApplyTheCombatStateToTheGameState() {
		underTest.playPhase(gameState);

		verify(gameState).applyCombat(combatState);
	}
}