package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.config.KryoNetwork;
import com.nsxwing.common.networking.io.event.GameEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;

@Slf4j
public class GameServer {

	private final GameResponseListener listener;
	private final Server server;
	protected Consumer<EndPoint> registrar = KryoNetwork::register;

	private boolean isRunning;

	public GameServer(Server server, GameResponseListener listener) {
		this.server = server;
		this.listener = listener;
	}

	public void start() throws IOException {
		registrar.accept(server);

		server.bind(PORT);
		server.addListener(listener);

		server.start();
		isRunning = true;
	}

	public void broadcastEvent(GameEvent event) {
		if (isRunning) {
			server.sendToAllTCP(event);
		}
	}

	public boolean isRunning() {
		return isRunning;
	}
}
