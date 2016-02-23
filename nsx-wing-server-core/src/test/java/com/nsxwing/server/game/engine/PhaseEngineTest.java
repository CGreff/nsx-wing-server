package com.nsxwing.server.game.engine;

import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.server.game.rules.phase.ActivationPhase;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PlanningPhase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PhaseEngineTest {

	private PhaseEngine underTest;

	@Mock
	private PlanningPhase planningPhase;

	@Mock
	private ActivationPhase activationPhase;

	private List<Phase> phases;

	@Mock
	private GameResponse champResponse;

	@Mock
	private GameResponse scrubResponse;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		phases = asList(planningPhase, activationPhase);
		underTest = new PhaseEngine(phases);
	}

	@Test
	public void shouldBeAbleToGetCurrentPhase() {
		assertThat(underTest.getCurrentPhase(), instanceOf(PlanningPhase.class));
	}

	@Test
	public void shouldInvokeGameResponseForCurrentPhase() {
		underTest.handleResponse(champResponse);

		verify(planningPhase).applyResponse(champResponse);
	}

	@Test
	public void shouldTransitionPhaseWhenPhaseIsFinished() {
		when(planningPhase.finished()).thenReturn(false);
		underTest.handleResponse(champResponse);

		assertThat(underTest.getCurrentPhase(), is(planningPhase));

		when(planningPhase.finished()).thenReturn(true);
		underTest.handleResponse(scrubResponse);

		assertThat(underTest.getCurrentPhase(), is(activationPhase));
	}

	@Test
	public void shouldResetCounterToZeroWhenExceedingListBounds() {
		when(planningPhase.finished()).thenReturn(true);
		when(activationPhase.finished()).thenReturn(true);
		underTest.handleResponse(champResponse);
		underTest.handleResponse(scrubResponse);

		assertThat(underTest.getCurrentPhase(), is(planningPhase));
	}
}