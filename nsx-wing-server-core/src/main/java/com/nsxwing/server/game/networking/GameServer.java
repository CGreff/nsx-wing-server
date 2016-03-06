package com.nsxwing.server.game.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.config.KryoNetwork;
import com.nsxwing.common.networking.io.event.GameEvent;
import com.nsxwing.common.networking.io.response.GameResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;

@Slf4j
public class GameServer {

	private final Server server;
	protected Consumer<EndPoint> registrar = KryoNetwork::register;

	private boolean isRunning;

	public GameServer(Server server) {
		this.server = server;
	}

	public void start(GameResponseListener listener) throws IOException {
		registrar.accept(server);

		server.bind(PORT);
		server.addListener(listener);

		server.start();
		isRunning = true;
	}

	@SneakyThrows
	public void stop() {
		server.stop();
		server.dispose();
		isRunning = false;
	}

	public void broadcastEvent(GameEvent event) {
		if (isRunning) {
			server.sendToAllTCP(event);
		}
	}

	public void sendToClient(Connection connection, GameResponse response) {
		server.sendToTCP(connection.getID(), response);
	}

	public boolean isRunning() {
		return isRunning;
	}
}
