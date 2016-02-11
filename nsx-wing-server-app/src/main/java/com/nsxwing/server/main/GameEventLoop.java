package com.nsxwing.server.main;

import com.nsxwing.common.event.server.ActionEvent;
import com.nsxwing.server.networking.AgentCommunicator;

import java.net.ServerSocket;

public class GameEventLoop implements Runnable {

	private AgentCommunicator playerOneCommunicator;
	private AgentCommunicator playerTwoCommunicator;

	public GameEventLoop(ServerSocket playerOneSocket, ServerSocket playerTwoSocket) {
		playerOneCommunicator = new AgentCommunicator(playerOneSocket);
		playerTwoCommunicator = new AgentCommunicator(playerTwoSocket);
	}

	@Override
	public void run() {
		while(true) {
			try {
				playerOneCommunicator.send(new ActionEvent());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
