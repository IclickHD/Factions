package io.github.iclickhd.factions.configs;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.Faction;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class FactionConfig extends AbstractCacheableConfig {
	public FactionConfig(Path configFile) {
		super(configFile);
	}
	
	public void populate() {
		get().getNode("factions").setComment("This stores all the data of the factions.");

		Faction safezoneFaction = new Faction(UUID.randomUUID(), "SafeZone");
		
		Faction warzoneFaction = new Faction(UUID.randomUUID(), "WarZone");
		
		Faction wildernessFaction = new Faction(UUID.randomUUID(), "Wilderness");

		try {
			get().getNode("factions", safezoneFaction.getUniqueId().toString()).setValue(TypeToken.of(Faction.class), safezoneFaction);
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}

		try {
			get().getNode("factions", warzoneFaction.getUniqueId().toString()).setValue(TypeToken.of(Faction.class), warzoneFaction);
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		
		try {
			get().getNode("factions", wildernessFaction.getUniqueId().toString()).setValue(TypeToken.of(Faction.class), wildernessFaction);
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	public void fillCache() {
		Map<UUID, Faction> factionsMap = new HashMap<UUID, Faction>();
		
		if (get().getNode("factions").getValue() != null) 
		{
			for (Object object : get().getNode("factions").getChildrenMap().keySet()) 
			{
				String factionUniqueId = String.valueOf(object);
				try 
				{
					Faction faction = get().getNode("factions", factionUniqueId).getValue(TypeToken.of(Faction.class));
					factionsMap.put(UUID.fromString(factionUniqueId), faction);
				} 
				catch (ObjectMappingException e) 
				{
					e.printStackTrace();
				}
			}
		}

		getPlugin().getFactionsCache().setFactionsCache(factionsMap);
	}
}
