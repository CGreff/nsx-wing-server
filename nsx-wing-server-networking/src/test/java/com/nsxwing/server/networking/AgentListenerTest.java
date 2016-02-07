package com.nsxwing.server.networking;

import com.nsxwing.server.event.GameEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AgentListenerTest {

    private AgentListener underTest;

    @Mock
    private ServerSocket serverSocket;

	@Mock
	private Socket socket;

    @Before
    public void setUp() throws IOException {
		MockitoAnnotations.initMocks(this);

        underTest = new AgentListener(serverSocket);

		when(serverSocket.accept()).thenReturn(socket);
    }

    @Test
    public void shouldTakeAServerSocketAndAcceptAConnection() throws IOException {
		//Construction happens in setUp()

		verify(serverSocket).accept();
    }

	@Test
	public void shouldReturnAGameEvent() {
		assertThat(underTest.listen(), instanceOf(GameEvent.class));
	}
}