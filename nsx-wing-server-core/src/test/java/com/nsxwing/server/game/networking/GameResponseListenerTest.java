package com.nsxwing.server.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import com.nsxwing.server.game.coordination.GameCoordinator;
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
	public void shouldHandOffReceivedResponseToGameCoordinator() {
		underTest.received(connection, response);

		verify(gameCoordinator).handleResponse(response);
		verifyNoMoreInteractions(gameCoordinator);
	}

	@Test
	public void shouldCreateAPlayerWhenCalledWithConnectionEvent() {
		ConnectionEvent connectionEvent = new ConnectionEvent();
		underTest.received(connection, connectionEvent);

		verify(gameCoordinator).connectPlayer(connection, connectionEvent);
		verifyNoMoreInteractions(gameCoordinator);
	}

	@Test
	public void shouldDoNothingWhenReceivingBadObject() {
		underTest.received(connection, new String());

		verifyZeroInteractions(gameCoordinator);
	}
}