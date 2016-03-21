package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.event.PlanningEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.common.networking.io.response.PlanningResponse;
import com.nsxwing.common.position.Forward;
import com.nsxwing.common.position.Maneuver;
import com.nsxwing.common.position.ManeuverDifficulty;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.nsxwing.common.position.ManeuverDifficulty.GREEN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlanningPhaseTest {

	@InjectMocks
	private PlanningPhase underTest;

	@Mock
	private GameServer gameServer;

	@Mock
	private GameState gameState;

	@Mock
	private PlanningResponse gameResponse;

	private int counter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		//This is used to test the asynchronous nature of waiting for events
		underTest.threadSleeper = this::handleSleep;
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
	public void shouldPlayPhaseAndReturnGameState() {
		GameState result = underTest.playPhase(gameState);

		assertThat(result, is(gameState));
	}

	@Test
	public void shouldSendAPlanningEvent() {
		underTest.playPhase(gameState);

		verify(gameServer).broadcastEvent(any(PlanningEvent.class));
	}

	@Test
	public void shouldHandleResponseAndAddManeuversToGameState() {
		Map<Integer, Maneuver> maneuvers = Collections.singletonMap(1, new Forward(1, GREEN));
		doReturn(maneuvers).when(gameResponse).getAgentManeuvers();

		GameState result = underTest.handleResponse(gameResponse);

	}
}