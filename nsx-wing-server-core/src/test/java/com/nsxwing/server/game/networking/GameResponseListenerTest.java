package com.nsxwing.server.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.server.game.GameCoordinator;
import com.nsxwing.server.game.engine.PhaseEngine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameResponseListenerTest {

	@InjectMocks
	private GameResponseListener underTest;

	@Mock
	private PhaseEngine phaseEngine;

	@Mock
	private GameCoordinator gameCoordinator;

	@Mock
	private Connection connection;

	@Mock
	private GameResponse response;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldHandOffReceivedResponseToPhaseEngine() {
		underTest.received(connection, response);

		verify(phaseEngine).handleResponse(response);
		verifyNoMoreInteractions(phaseEngine);
	}

	@Test
	public void shouldCreateAPlayerWhenCalledWithConnectionEvent() {
		underTest.received(connection, new ConnectionEvent());

		verify(gameCoordinator).connectPlayer(connection);
	}

	@Test
	public void shouldDoNothingWhenReceivingBadObject() {
		underTest.received(connection, new String());

		verifyZeroInteractions(phaseEngine);
	}
}