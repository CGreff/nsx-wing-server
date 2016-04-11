package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
import com.nsxwing.common.state.CombatState;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.networking.combat.AttackResponseHandler;
import com.nsxwing.server.game.networking.combat.CombatResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyAttackResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyEvadeResponseHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CombatPhaseTest {
	private CombatPhase underTest;

	@Mock
	private GameServer gameServer;

	@Mock
	private Map<Class, CombatResponseHandler> responseHandlers;

	@Mock
	private GameState gameState;

	@Mock
	private CombatState combatState;

	@Mock
	private AttackResponseHandler attackResponseHandler;

	@Mock
	private ModifyAttackResponseHandler modifyAttackResponseHandler;

	@Mock
	private ModifyEvadeResponseHandler modifyEvadeResponseHandler;

	private int counter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		//This is used to test the asynchronous nature of waiting for events
		setupResponseHandlers();

		underTest = new CombatPhase(gameServer, responseHandlers);
		underTest.threadSleeper = this::handleSleep;
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

	/*
	 *  This method will alternately return either the scrub
	 *  or champ player as having responded to an event.
	 */
	private void handleSleep(long millis) {
		counter++;
		if (counter % 2 == 0) {
			underTest.handledScrub = true;
		} else {
			underTest.handledChamp = true;
		}
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
}