package com.nsxwing.server.integration.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.component.pilot.Pilot;
import com.nsxwing.common.gameplay.action.Focus;
import com.nsxwing.common.gameplay.meta.combat.Target;
import com.nsxwing.common.gameplay.meta.modifiers.NoOpModifier;
import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.common.networking.io.event.AttackEvent;
import com.nsxwing.common.networking.io.event.ConnectionEvent;
import com.nsxwing.common.networking.io.event.ModifyAttackEvent;
import com.nsxwing.common.networking.io.event.ModifyEvadeEvent;
import com.nsxwing.common.networking.io.event.PlanningEvent;
import com.nsxwing.common.networking.io.response.ActionResponse;
import com.nsxwing.common.networking.io.response.AttackResponse;
import com.nsxwing.common.networking.io.response.ConnectionResponse;
import com.nsxwing.common.networking.io.response.ModifyAttackResponse;
import com.nsxwing.common.networking.io.response.ModifyEvadeResponse;
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
import static java.util.UUID.randomUUID;

@Slf4j
public class MockClient {

	private Client client;
	private PlayerIdentifier playerIdentifier;
	private String agentIdentifier;

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
			agentIdentifier = randomUUID().toString();
			agent = new PlayerAgent();
			agent.setAgentId(agentIdentifier);
			agent.setPosition(new Position(new Coordinate(0, 0), 0));
			agent.setPilot(new Pilot(5, 3, 3));
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
			} else if (object instanceof AttackEvent) {
				client.sendTCP(buildAttackResponse((AttackEvent) object));
			} else if (object instanceof ModifyAttackEvent) {
				client.sendTCP(buildModifyAttackResponse());
			} else if (object instanceof ModifyEvadeEvent) {
				client.sendTCP(buildModifyEvadeResponse());
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
			HashMap<String, Maneuver> agentManeuvers = new HashMap<>();
			Maneuver maneuver = new Forward(0, GREEN);
			agentManeuvers.put(agentIdentifier, maneuver);
			response.setAgentManeuvers(agentManeuvers);
			return response;
		}

		private AttackResponse buildAttackResponse(AttackEvent event) {
			AttackResponse response = new AttackResponse();
			response.setPlayerIdentifier(playerIdentifier);
			response.setTarget(chooseTarget(event));
			return response;
		}
	}

	private Target chooseTarget(AttackEvent event) {
		if (event.getTargets().isEmpty()) {
			return null;
		}

		return event.getTargets().get(0);
	}

	private ModifyAttackResponse buildModifyAttackResponse() {
		ModifyAttackResponse response = new ModifyAttackResponse();
		response.setPlayerIdentifier(playerIdentifier);
		response.setDiceModifiers(asList(new NoOpModifier()));
		return response;
	}

	private ModifyEvadeResponse buildModifyEvadeResponse() {
		ModifyEvadeResponse response = new ModifyEvadeResponse();
		response.setPlayerIdentifier(playerIdentifier);
		response.setDiceModifiers(asList(new NoOpModifier()));
		return response;
	}
}
