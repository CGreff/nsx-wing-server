package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.event.GameEvent;

import java.io.IOException;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;
import static com.nsxwing.common.networking.config.KryoNetwork.register;

public class GameServer {

	private Server server;

	public GameServer(Server server) throws IOException {
		this.server = server;

		register(server);

		server.bind(PORT);
		server.start();

		server.addListener(new GameEventListener());
	}

	public void broadcastEvent(GameEvent event) {
		server.sendToAllTCP(event);
	}


}

