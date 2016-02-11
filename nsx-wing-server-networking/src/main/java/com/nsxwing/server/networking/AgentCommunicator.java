package com.nsxwing.server.networking;

import com.nsxwing.common.event.GameEvent;
import com.nsxwing.common.event.server.ActionEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class AgentCommunicator implements Runnable {

	private Socket socket;

	@SneakyThrows
	public AgentCommunicator(ServerSocket serverSocket) {
		this.socket = serverSocket.accept();
	}

	@SneakyThrows
	public void send(GameEvent event) {
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.writeObject(event);
		outputStream.reset();
	}

	public GameEvent listen(Class<? extends GameEvent> clazz) {
		GameEvent event = new ActionEvent();

		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			if (inputStream.available() > 0) {
				event = clazz.cast(inputStream.readObject());
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
