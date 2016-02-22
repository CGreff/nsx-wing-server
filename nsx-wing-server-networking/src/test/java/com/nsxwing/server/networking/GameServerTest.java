package com.nsxwing.server.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.io.event.GameEvent;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class GameServerTest {

	private GameServer underTest;

	@Mock
	private Server server;

	@Mock
	private Kryo kryo;

	@Mock
	private GameEvent event;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		doReturn(kryo).when(server).getKryo();
	}

	@Test
	@SneakyThrows
	public void shouldStartServer() {
		underTest = new GameServer(server);

		verify(server).start();
	}

	@Test
	@SneakyThrows
	public void shouldBindToCorrectPort() {
		underTest = new GameServer(server);

		verify(server).bind(PORT);
	}

	@Test
	@SneakyThrows
	public void shouldAddAGameEventListener() {
		underTest = new GameServer(server);

		verify(server).addListener(isA(GameResponseListener.class));
	}

	@Test
	@SneakyThrows
	public void shouldBroadcastSpecifiedEvent() {
		underTest = new GameServer(server);
		underTest.broadcastEvent(event);

		verify(server).sendToAllTCP(event);
	}
}