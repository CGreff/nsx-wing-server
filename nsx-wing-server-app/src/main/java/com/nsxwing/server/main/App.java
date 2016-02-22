package com.nsxwing.server.main;

import com.esotericsoftware.kryonet.Server;
import com.nsxwing.common.networking.io.event.ActionEvent;
import com.nsxwing.server.networking.GameServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {
		try {
			GameServer gameServer = new GameServer(new Server());
			while (true) {
				gameServer.broadcastEvent(new ActionEvent());
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			System.out.println("Whoopsie daisy.");
		}
	}

}
