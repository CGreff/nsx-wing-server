package com.nsxwing.server.config;

import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.game.rules.phase.ActivationPhase;
import com.nsxwing.server.game.rules.phase.CombatPhase;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PlanningPhase;
import com.nsxwing.server.networking.GameServer;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AppConfigurationTest {

	@Test
	public void shouldProvideAPhaseList() {
		List<Phase> result = AppConfiguration.getPhases();

		assertThat(result, instanceOf(List.class));
		assertThat(result.get(0), instanceOf(PlanningPhase.class));
		assertThat(result.get(1), instanceOf(ActivationPhase.class));
		assertThat(result.get(2), instanceOf(CombatPhase.class));
	}

	@Test
	public void shouldProvideAPhaseEngine() {
		PhaseEngine result = AppConfiguration.getPhaseEngine();

		assertThat(result, instanceOf(PhaseEngine.class));
	}

	@Test
	public void shouldProvideAGameServer() {
		PhaseEngine phaseEngine = mock(PhaseEngine.class);
		GameServer result = AppConfiguration.initGameServer(phaseEngine);

		assertThat(result, instanceOf(GameServer.class));
		assertTrue(result.isRunning());
	}
}