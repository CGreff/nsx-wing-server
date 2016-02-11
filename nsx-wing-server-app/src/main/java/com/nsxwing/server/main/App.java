package com.nsxwing.server.main;

import com.nsxwing.server.networking.GameServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {
		try {
			GameServer gameServer = new GameServer();
			while (true) {
				gameServer.broadcastActionEvent();
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			System.out.println("Whoopsie daisy.");
		}
	}

}
