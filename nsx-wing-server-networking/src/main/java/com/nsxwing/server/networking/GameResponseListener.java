package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.networking.io.response.GameResponse;

public class GameResponseListener extends Listener {

	@Override
	public void received (Connection connection, Object object) {
		if (object instanceof GameResponse) {
			GameResponse event = (GameResponse) object;
			System.out.println("HEYOOO");
			System.out.println(event.getClass());
		}
	}

}
