package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.gameplay.action.Action;
import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.common.networking.io.response.ActionResponse;
import com.nsxwing.common.player.Player;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.position.Maneuver;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.nsxwing.common.player.PlayerIdentifier.CHAMP;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ActivationPhaseTest {

	private static final int AGENT_ID = 0;

	@InjectMocks
	private ActivationPhase underTest;

	@Mock
	private GameServer gameServer;

	@Mock
	private GameState gameState;

	@Mock
	private GameState transferrableGameState;

	@Mock
	private ActionResponse gameResponse;

	@Mock
	private Action action;

	@Mock
	private Player player;

	@Mock
	private PlayerAgent agent;

	@Mock
	private Maneuver maneuver;

	@Mock
	private Function<GameState, GameState> gameStateManeuverStripper;

	private List<PlayerAgent> agents;
	private Map<Integer, Maneuver> plannedManeuvers;
	private int connectionId = 1;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		mockGameState();

		//This is used to test the asynchronous nature of waiting for events
		underTest.threadSleeper = this::handleSleep;
		underTest.gameStateManeuverStripper = gameStateManeuverStripper;

		doReturn(action).when(gameResponse).getAction();
		doReturn(transferrableGameState).when(gameStateManeuverStripper).apply(gameState);
	}

	private void mockGameState() {
		agents = asList(agent);
		plannedManeuvers = singletonMap(AGENT_ID, maneuver);
		doReturn(CHAMP).when(agent).getOwner();
		doReturn(AGENT_ID).when(agent).getAgentId();
		doReturn(connectionId).when(player).getConnection();
		doReturn(agents).when(transferrableGameState).getPlayerAgents();
		doReturn(player).when(transferrableGameState).getPlayerFor(CHAMP);
		doReturn(plannedManeuvers).when(gameState).getPlannedManeuvers();
	}

	/*
	 *  This method will alternately return either the scrub
	 *  or champ player as having responded to an event.
	 */
	private void handleSleep(long millis) {
		underTest.handledChamp = true;
	}

	@Test
	public void shouldPlayPhaseAndReturnGameState() {
		GameState result = underTest.playPhase(gameState);

		assertThat(result, is(transferrableGameState));
	}

	@Test
	public void shouldHandleResponseByExecutingTheAction() {
		GameState postActionState = mock(GameState.class);
		doReturn(postActionState).when(action).execute(gameState);
		underTest.currentGameState = gameState;

		GameState result = underTest.handleResponse(gameResponse);

		assertThat(result, is(postActionState));
		assertThat(underTest.currentGameState, is(postActionState));
		verify(gameResponse).getAction();
		verify(action).execute(gameState);
	}

	@Test
	public void shouldSendAnActionRequestToPlayerAgents() {
		underTest.playPhase(gameState);

		verify(gameServer).sendToClient(eq(connectionId), eq(new ActionEvent(transferrableGameState)));
	}

	@Test
	public void shouldManeuverEachPlayerAgentAccordingToItsChoice() {
		underTest.playPhase(gameState);

		verify(transferrableGameState).maneuverAgent(eq(AGENT_ID), eq(maneuver));
	}
}