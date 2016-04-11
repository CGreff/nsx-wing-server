package com.nsxwing.server.game.networking.combat;

import com.nsxwing.common.gameplay.meta.dice.EvadeDie;
import com.nsxwing.common.gameplay.meta.modifiers.DiceModifer;
import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
import com.nsxwing.common.state.CombatState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class ModifyEvadeResponseHandlerTest {
	@InjectMocks
	private ModifyEvadeResponseHandler underTest;

	@Mock
	private CombatState combatState;

	@Mock
	private ModifyEvadeResponse response;

	@Mock
	private DiceModifer firstDiceModifer;

	@Mock
	private DiceModifer secondDiceModifer;

	@Mock
	private List<EvadeDie> evadeDice;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		doReturn(asList(firstDiceModifer, secondDiceModifer)).when(response).getDiceModifiers();
		doReturn(evadeDice).when(combatState).getEvadeDice();
	}

	@Test
	public void shouldCallEveryDiceModifierForTheEvadeDice() {
		CombatState result = underTest.handleResponse(response, combatState);

		assertThat(result, is(combatState));
		verify(firstDiceModifer).modify(evadeDice);
		verify(secondDiceModifer).modify(evadeDice);
	}
}