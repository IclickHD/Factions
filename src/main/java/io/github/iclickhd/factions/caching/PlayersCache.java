package io.github.iclickhd.factions.caching;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.iclickhd.factions.models.FactionPlayer;
import io.github.iclickhd.factions.objects.AbstractPluginObject;

public class PlayersCache extends AbstractPluginObject {
	private static Map<UUID, FactionPlayer> factionPlayersCache = new HashMap<UUID, FactionPlayer>();

	public Map<UUID, FactionPlayer> getCache() {
		return factionPlayersCache;
	}

	public void setFactionPlayersCache(Map<UUID, FactionPlayer> factionPlayersCache) {
		PlayersCache.factionPlayersCache = factionPlayersCache;
	}
}
