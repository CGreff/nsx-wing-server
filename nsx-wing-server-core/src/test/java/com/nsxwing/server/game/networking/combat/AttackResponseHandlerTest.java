package com.nsxwing.server.game.networking.combat;

import com.nsxwing.common.gameplay.meta.combat.Target;
import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.state.CombatState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AttackResponseHandlerTest {
	@InjectMocks
	private AttackResponseHandler underTest;

	@Mock
	private CombatState combatState;

	@Mock
	private AttackResponse response;

	@Mock
	private Target target;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		doReturn(target).when(response).getTarget();
	}

	@Test
	public void shouldSetTheTarget() {
		CombatState result = underTest.handleResponse(response, combatState);

		assertThat(result, is(combatState));
		verify(combatState).setDefender(target);
	}
}