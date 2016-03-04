package com.nsxwing.server.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.common.networking.io.event.GameEvent;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;
import static org.mockito.Mockito.doReturn;
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
		underTest.start();

		verify(server).start();
	}

	@Test
	@SneakyThrows
	public void shouldRegisterSerializationClasses() {
		underTest.start();

		verify(registrar).accept(server);
	}

	@Test
	@SneakyThrows
	public void shouldBindToCorrectPort() {
		underTest.start();

		verify(server).bind(PORT);
	}

	@Test
	@SneakyThrows
	public void shouldAddAGameEventListener() {
		underTest.start();

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
		underTest.start();
		underTest.broadcastEvent(event);

		verify(server).sendToAllTCP(event);
	}
}