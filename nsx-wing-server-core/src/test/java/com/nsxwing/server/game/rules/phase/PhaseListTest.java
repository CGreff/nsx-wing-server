package com.nsxwing.server.game.rules.phase;

import com.nsxwing.server.game.networking.GameServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PhaseListTest {

	private PhaseList underTest;

	@Mock
	private GameServer gameServer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		underTest = new PhaseList(gameServer);
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