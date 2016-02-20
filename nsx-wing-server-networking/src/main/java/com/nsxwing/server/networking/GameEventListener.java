package com.nsxwing.server.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nsxwing.common.event.GameEvent;

public class GameEventListener extends Listener {

	@Override
	public void received (Connection connection, Object object) {
		if (object instanceof GameEvent) {
			GameEvent event = (GameEvent) object;
			System.out.println(event.getEventType());
		}
	}

}
