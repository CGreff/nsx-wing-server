package com.nsxwing.server.game.rules.phase;

import com.nsxwing.server.game.networking.GameServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PlanningPhaseTest {
	@InjectMocks
	private PlanningPhase underTest;

	@Mock
	private GameServer gameServer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	//So this presently feels inherently un-unit-testable.
	//I'll come back to it.
	//TODO: Come back to it.
}