package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.networking.io.response.GameResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameResponseListener extends Listener {

	@Override
	public void received (Connection connection, Object object) {
		if (object instanceof GameResponse) {
			GameResponse event = (GameResponse) object;
			log.info(event.getClass().toString());
		}
	}

}
