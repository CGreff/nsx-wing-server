package com.nsxwing.server.networking;

import com.nsxwing.common.event.GameEvent;
import com.nsxwing.common.event.server.ActionEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AgentCommunicatorTest {

    private AgentCommunicator underTest;

    @Mock
    private ServerSocket serverSocket;

	@Mock
	private Socket socket;

    @Before
    public void setUp() throws IOException {
		MockitoAnnotations.initMocks(this);

        underTest = new AgentCommunicator(serverSocket);

		when(serverSocket.accept()).thenReturn(socket);
    }

    @Test
    public void shouldTakeAServerSocketAndAcceptAConnection() throws IOException {
		//Construction happens in setUp()

		verify(serverSocket).accept();
    }

	@Test
	public void shouldReturnAGameEvent() {
		assertThat(underTest.listen(ActionEvent.class), instanceOf(GameEvent.class));
	}
}