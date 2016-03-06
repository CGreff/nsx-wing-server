package com.nsxwing.server.game.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.io.event.GameEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameServerTest {

	@InjectMocks
	private GameServer underTest;

	@Mock
	private Consumer<EndPoint> registrar;

	@Mock
	private Server server;

	@Mock
	private GameResponseListener listener;

	@Mock
	private Kryo kryo;

	@Mock
	private GameEvent event;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		doReturn(kryo).when(server).getKryo();
		//Ghetto self-injection because Mockito won't do both field & constructor injection.
		underTest.registrar = registrar;
	}

	@Test
	@SneakyThrows
	public void shouldStartServer() {
		underTest.start(listener);

		verify(server).start();
	}

	@Test
	@SneakyThrows
	public void shouldRegisterSerializationClasses() {
		underTest.start(listener);

		verify(registrar).accept(server);
	}

	@Test
	@SneakyThrows
	public void shouldBindToCorrectPort() {
		underTest.start(listener);

		verify(server).bind(PORT);
	}

	@Test
	@SneakyThrows
	public void shouldAddAGameEventListener() {
		underTest.start(listener);

		verify(server).addListener(listener);
	}

	@Test
	public void shouldNotBroadcastIfServerHasntStarted() {
		underTest.broadcastEvent(event);

		verifyZeroInteractions(server);
	}

	@Test
	@SneakyThrows
	public void shouldBroadcastSpecifiedEvent() {
		underTest.start(listener);
		underTest.broadcastEvent(event);

		verify(server).sendToAllTCP(event);
	}

	@Test
	public void shouldBeAbleToRespondToIndividualClients() {
		Connection connection = mock(Connection.class);
		doReturn(1).when(connection).getID();
		GameResponse response = mock(GameResponse.class);

		underTest.sendToClient(connection, response);

		verify(connection).getID();
		verify(server).sendToTCP(1, response);
	}

	@Test
	@SneakyThrows
	public void shouldReportIfItsRunning() {
		underTest.start(listener);

		assertTrue(underTest.isRunning());
	}

	@Test
	@SneakyThrows
	public void shouldStopServer() {
		underTest.start(listener);
		assertTrue(underTest.isRunning());

		underTest.stop();
		assertFalse(underTest.isRunning());
		verify(server).stop();
		verify(server).dispose();
	}
}