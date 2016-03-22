package com.nsxwing.server.integration.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.component.pilot.Pilot;
import com.nsxwing.common.gameplay.action.Focus;
import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.event.PlanningEvent;
import com.nsxwing.common.networking.io.response.ActionResponse;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.networking.io.response.PlanningResponse;
import com.nsxwing.common.player.PlayerIdentifier;
import com.nsxwing.common.player.agent.PlayerAgent;
import com.nsxwing.common.position.Forward;
import com.nsxwing.common.position.Maneuver;
import com.nsxwing.common.position.descriptor.Coordinate;
import com.nsxwing.common.position.descriptor.Position;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static com.nsxwing.common.networking.config.KryoNetwork.PORT;
import static com.nsxwing.common.networking.config.KryoNetwork.register;
import static com.nsxwing.common.position.ManeuverDifficulty.GREEN;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@Slf4j
public class MockClient {

	private Client client;
	private PlayerIdentifier playerIdentifier;

	public MockClient() {
		MockitoAnnotations.initMocks(this);
		client = new Client();
		client.start();
		register(client);
		client.getKryo();
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
		private PlayerAgent agent;

		public void connected(Connection connection) {
			log.info("Client has connected to localhost - sending connectionEvent");
			client.sendTCP(buildConnectionEvent());
		}

		private ConnectionEvent buildConnectionEvent() {
			ConnectionEvent connectionEvent = new ConnectionEvent();
			agent = new PlayerAgent();
			agent.setAgentId(0);
			agent.setPosition(new Position(new Coordinate(0, 0), 0));
			agent.setPilot(new Pilot(5));
			connectionEvent.setPlayerAgents(asList(agent));
			return connectionEvent;
		}

		public void received(Connection connection, Object object) {
			log.info("Client has received Object:" + object);
			if (object instanceof ConnectionResponse) {
				playerIdentifier = ((ConnectionResponse) object).getPlayerIdentifier();
			} else if (object instanceof PlanningEvent) {
				client.sendTCP(buildPlanningResponse());
			} else if (object instanceof ActionEvent) {
				client.sendTCP(buildActionResponse());
			}
		}

		private ActionResponse buildActionResponse() {
			ActionResponse response = new ActionResponse();
			response.setPlayerIdentifier(playerIdentifier);
			response.setAction(new Focus(agent));
			return response;
		}

		private PlanningResponse buildPlanningResponse() {
			PlanningResponse response = new PlanningResponse();
			response.setPlayerIdentifier(playerIdentifier);
			HashMap<Integer, Maneuver> agentManeuvers = new HashMap<>();
			Maneuver maneuver = new Forward(0, GREEN);
			agentManeuvers.put(0, maneuver);
			response.setAgentManeuvers(agentManeuvers);
			return response;
		}
	}
}
