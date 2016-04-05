package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.PlanningResponse;
import com.nsxwing.common.state.GameState;
import com.nsxwing.server.game.networking.GameServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CombatPhaseTest {
	@InjectMocks
	private CombatPhase underTest;

	@Mock
	private GameServer gameServer;

	@Mock
	private GameState gameState;

	@Mock
	private AttackResponse attackResponse;

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
}