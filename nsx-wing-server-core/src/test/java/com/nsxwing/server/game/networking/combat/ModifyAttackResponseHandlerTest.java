package com.nsxwing.server.game.networking.combat;

import com.nsxwing.common.gameplay.meta.dice.AttackDie;
import com.nsxwing.common.gameplay.meta.modifiers.DiceModifer;
import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
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

public class ModifyAttackResponseHandlerTest {
	@InjectMocks
	private ModifyAttackResponseHandler underTest;

	@Mock
	private CombatState combatState;

	@Mock
	private ModifyAttackResponse response;

	@Mock
	private DiceModifer firstDiceModifer;

	@Mock
	private DiceModifer secondDiceModifer;

	@Mock
	private List<AttackDie> attackDice;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		doReturn(asList(firstDiceModifer, secondDiceModifer)).when(response).getDiceModifiers();
		doReturn(attackDice).when(combatState).getAttackDice();
	}

	@Test
	public void shouldCallEveryDiceModifierForTheAttackDice() {
		CombatState result = underTest.handleResponse(response, combatState);

		assertThat(result, is(combatState));
		verify(firstDiceModifer).modify(attackDice);
		verify(secondDiceModifer).modify(attackDice);
	}
}