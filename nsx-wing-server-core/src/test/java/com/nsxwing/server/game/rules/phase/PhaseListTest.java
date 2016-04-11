package com.nsxwing.server.game.rules.phase;

import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.networking.combat.AttackResponseHandler;
import com.nsxwing.server.game.networking.combat.CombatResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyAttackResponseHandler;
import com.nsxwing.server.game.networking.combat.ModifyEvadeResponseHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PhaseListTest {

	@InjectMocks
	private PhaseList underTest;

	@Mock
	private GameServer gameServer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
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

	@Test
	public void shouldCreateThreeResponseHandlers() {
		Map<Class, CombatResponseHandler> result = underTest.createCombatResponseHandlers();

		assertThat(result.get(AttackResponse.class), instanceOf(AttackResponseHandler.class));
		assertThat(result.get(ModifyAttackResponse.class), instanceOf(ModifyAttackResponseHandler.class));
		assertThat(result.get(ModifyEvadeResponse.class), instanceOf(ModifyEvadeResponseHandler.class));
	}
}