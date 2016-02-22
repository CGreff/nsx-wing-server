package com.nsxwing.server.game.engine;

import com.nsxwing.server.game.rules.phase.PlanningPhase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class PhaseEngineTest {

	private PhaseEngine underTest;

	@Before
	public void setUp() {
		underTest = new PhaseEngine();
	}

	@Test
	public void shouldBeAbleToGetCurrentPhase() {
		assertThat(underTest.getCurrentPhase(), instanceOf(PlanningPhase.class));
	}

	@Test
	public void shouldAcceptAGameResponse() {

	}
}