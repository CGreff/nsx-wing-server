package com.nsxwing.server.game.rules.phase;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PhaseListTest {

	private PhaseList underTest;

	@Before
	public void setUp() {
		underTest = new PhaseList();
	}

	@Test
	public void shouldReturnTheCurrentPhase() {
		Phase expected = underTest.getCurrentPhase();

		assertThat(expected, instanceOf(PlanningPhase.class));
	}

	@Test
	public void shouldCorrectlyIncrementThePhase() {
		Phase initial = underTest.getCurrentPhase();
		underTest.incrementPhase();
		Phase expected = underTest.getCurrentPhase();

		assertThat(expected, instanceOf(ActivationPhase.class));
		assertThat(expected, is(not(initial)));
	}

	@Test
	public void shouldReturnListOfPhases() {
		List<Phase> expected = underTest.getPhases();

		assertThat(expected.size(), is(3));
	}
}