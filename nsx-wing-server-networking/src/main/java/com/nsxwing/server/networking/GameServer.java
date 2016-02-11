package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.event.server.ActionEvent;
import com.nsxwing.common.networking.config.KryoNetwork;

import java.io.IOException;

public class GameServer {

	private Server server;

	public GameServer() throws IOException {
		server = new Server();

		KryoNetwork.register(server);

		server.bind(KryoNetwork.port);
		server.start();
	}

	public void broadcastActionEvent() {
		server.sendToAllTCP(new ActionEvent());
	}
}

