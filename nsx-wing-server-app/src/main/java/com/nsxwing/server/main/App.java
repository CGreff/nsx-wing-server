package com.nsxwing.server.main;

import com.nsxwing.common.event.GameEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class App {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5413);
			Socket socket = serverSocket.accept();

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            for (int i = 0; i < 5; i++) {
				outputStream.writeObject(new GameEvent());
				log.info("Sent a game event");
				Thread.sleep(5000);
            }

			socket.close();
			serverSocket.close();
        } catch (Exception e) {
            log.debug("Something has gone awry.", e);
        }
    }

}
