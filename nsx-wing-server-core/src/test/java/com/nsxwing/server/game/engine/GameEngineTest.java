package com.nsxwing.server.game.engine;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.rules.phase.ActivationPhase;
import com.nsxwing.server.game.rules.phase.CombatPhase;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PhaseList;
import com.nsxwing.server.game.rules.phase.PlanningPhase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GameEngineTest {

	@InjectMocks
	private GameEngine underTest;

	@Mock
	private GameServer gameServer;

	@Mock
	private PhaseList phaseList;

	@Mock
	private Player champ;

	@Mock
	private Player scrub;

	@Mock
	private GameState gameState;

	@Mock
	private GameState planningGameState;

	@Mock
	private GameState activationGameState;

	@Mock
	private PlanningPhase planningPhase;

	@Mock
	private ActivationPhase activationPhase;

	@Mock
	private CombatPhase combatPhase;

	@Mock
	private List<Phase> phases;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		phases = asList(planningPhase, activationPhase, combatPhase);
		underTest.setChamp(champ);
		underTest.setScrub(scrub);

		mockPhases();
	}

	private void mockPhases() {
		doReturn(phases).when(phaseList).getPhases();
		doReturn(planningGameState).when(planningPhase).playPhase(gameState);
		doReturn(activationGameState).when(activationPhase).playPhase(planningGameState);
		doReturn(gameState).when(combatPhase).playPhase(activationGameState);
	}

	@Test
	public void shouldPassOffAResponseToTheCurrentPhase() {
		GameResponse response = mock(GameResponse.class);
		doReturn(activationPhase).when(phaseList).getCurrentPhase();

		underTest.handleResponse(response);

		verify(activationPhase).applyResponse(response);
	}

	@Test
	public void shouldIncrementCurrentTurnNumberWhenATurnIsPlayed() {
		doReturn(0).when(gameState).getTurnNumber();

		GameState result = underTest.playTurn(gameState);

		assertThat(result.getTurnNumber(), is(1));
	}

	@Test
	public void shouldTriggerEachPhase() {
		underTest.playTurn(gameState);

		verify(planningPhase).playPhase(gameState);
		verify(activationPhase).playPhase(planningGameState);
		verify(combatPhase).playPhase(activationGameState);
		verify(phaseList, times(3)).incrementPhase();
	}
}