package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.server.game.engine.PhaseEngine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GameResponseListenerTest {

	@InjectMocks
	private GameResponseListener underTest;

	@Mock
	private PhaseEngine phaseEngine;

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

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenReceivingBadObject() {
		underTest.received(connection, new String());
	}
}