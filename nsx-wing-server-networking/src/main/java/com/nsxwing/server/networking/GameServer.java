package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.io.event.GameEvent;

import java.io.IOException;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;
import static com.nsxwing.common.networking.config.KryoNetwork.register;

public class GameServer {

	private Server server;

	public GameServer(Server server, GameResponseListener listener) {
		this.server = server;

		register(server);

		//TODO: Distant future - use env property for this + client-side.
		try {
			server.bind(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.start();

		server.addListener(listener);
	}

	public void broadcastEvent(GameEvent event) {
		server.sendToAllTCP(event);
	}


}

