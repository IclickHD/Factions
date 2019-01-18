package io.github.iclickhd.factions.caching;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.objects.AbstractPluginObject;

public class FactionsCache extends AbstractPluginObject {
	private static Map<UUID, Faction> factionsCache = new HashMap<UUID, Faction>();

	public Map<UUID, Faction> getCache() {
		return factionsCache;
	}

	public void setFactionsCache(Map<UUID, Faction> factionsCache) {
		FactionsCache.factionsCache = factionsCache;
	}
}
