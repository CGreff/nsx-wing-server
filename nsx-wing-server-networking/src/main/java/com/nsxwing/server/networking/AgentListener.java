package com.nsxwing.server.networking;

import com.nsxwing.server.event.GameEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class AgentListener implements Runnable {

	private Socket socket;

	AgentListener(ServerSocket serverSocket) throws IOException {
		this.socket = serverSocket.accept();
	}

	public GameEvent listen() {
		GameEvent event = new GameEvent();

		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			if (inputStream.available() > 0) {
				event = (GameEvent) inputStream.readObject();
			}
		} catch (Exception e) {
			log.info("Oh gosh something went wrong", e);
		}

		return event;
	}

	@Override
	public void run() {

	}
}
