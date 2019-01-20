package io.github.iclickhd.factions.configs;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.FactionPlayer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class PlayerConfig extends AbstractCacheableConfig {

	public PlayerConfig(Path configFile) {
		super(configFile);
		this.setup();
	}

	@Override
	public void populate() {
		get().getNode("players").setComment("This stores all the data of the players.");
	}

	@Override
	public void fillCache() {
		Map<UUID, FactionPlayer> factionPlayersMap = new HashMap<UUID, FactionPlayer>();
		
		if (get().getNode("players").getValue() != null) 
		{
			for (Object object : get().getNode("players").getChildrenMap().keySet()) 
			{
				String playerUniqueId = String.valueOf(object);
				try 
				{
					FactionPlayer factionPlayer = get().getNode("players", playerUniqueId).getValue(TypeToken.of(FactionPlayer.class));
					factionPlayersMap.put(UUID.fromString(playerUniqueId), factionPlayer);
				} 
				catch (ObjectMappingException e) 
				{
					e.printStackTrace();
				}
			}
		}

		getPlugin().getPlayersCache().setFactionPlayersCache(factionPlayersMap);
	}
}
