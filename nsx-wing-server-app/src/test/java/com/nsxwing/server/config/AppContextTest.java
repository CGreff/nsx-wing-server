package com.nsxwing.server.config;

import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.engine.PhaseEngine;
import com.nsxwing.server.game.networking.GameServer;
import com.nsxwing.server.game.rules.phase.ActivationPhase;
import com.nsxwing.server.game.rules.phase.CombatPhase;
import com.nsxwing.server.game.rules.phase.Phase;
import com.nsxwing.server.game.rules.phase.PlanningPhase;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AppContextTest {

	@Test
	public void shouldProvideAPhaseList() {
		List<Phase> result = AppContext.getPhases();

		assertThat(result, instanceOf(List.class));
		assertThat(result.get(0), instanceOf(PlanningPhase.class));
		assertThat(result.get(1), instanceOf(ActivationPhase.class));
		assertThat(result.get(2), instanceOf(CombatPhase.class));
	}

	@Test
	public void shouldProvideAPhaseEngine() {
		PhaseEngine result = AppContext.getPhaseEngine();

		assertThat(result, instanceOf(PhaseEngine.class));
	}

	@Test
	public void shouldProvideAGameServer() {
		GameServer result = AppContext.getGameServer();

		assertThat(result, instanceOf(GameServer.class));
	}

	@Test
	public void shouldProvideAGameCoordinator() {
		GameCoordinator result = AppContext.getGameCoordinator(mock(GameServer.class));

		assertThat(result, instanceOf(GameCoordinator.class));
	}

	@Test
	public void shouldStartAGameServer() {
		GameServer server = AppContext.getGameServer();
		AppContext.initGameServer(server, mock(PhaseEngine.class), mock(GameCoordinator.class));

		assertTrue(server.isRunning());
	}
}