package com.nsxwing.server.integration.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.player.agent.PlayerAgent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;
import static com.nsxwing.common.networking.config.KryoNetwork.register;
import static java.util.Arrays.asList;

@Slf4j
public class MockClient {

	private Client client;
	private PlayerIdentifier playerIdentifier;

	public MockClient() {
		client = new Client();
		client.start();
		register(client);
		client.addListener(new StubbedListener());
	}

	@SneakyThrows
	public void connect() {
		log.info("Client is connecting to localhost");
		client.connect(5000, "localhost", PORT);
	}

	public PlayerIdentifier getPlayerIdentifier() {
		return playerIdentifier;
	}

	private class StubbedListener extends Listener {
		public void connected(Connection connection) {
			log.info("Client has connected to localhost - sending connectionEvent");
			ConnectionEvent connectionEvent = new ConnectionEvent();
			connectionEvent.setPlayerAgents(asList(new PlayerAgent()));
			client.sendTCP(connectionEvent);
		}

		public void received(Connection connection, Object object) {
			log.info("Client has received Object:" + object);
			if (object instanceof ConnectionResponse) {
				playerIdentifier = ((ConnectionResponse) object).getPlayerIdentifier();
			}
		}
	}
}
