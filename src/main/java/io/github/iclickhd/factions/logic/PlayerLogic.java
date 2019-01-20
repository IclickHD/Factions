package io.github.iclickhd.factions.logic;

import org.spongepowered.api.scheduler.Task;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.FactionPlayer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class PlayerLogic extends AbstractLogic implements ICacheableLogic<FactionPlayer> {
	@Override
	public void addOrUpdate(FactionPlayer factionPlayer) {
		if (getPlugin().getPlayersCache().getCache().containsKey(factionPlayer.getUniqueId()))
        {
        	getPlugin().getPlayersCache().getCache().replace(factionPlayer.getUniqueId(), factionPlayer);
    		Task.builder().execute(() -> {
    			try {
					getPlugin().getPlayerConfig().get().getNode("players", factionPlayer.getUniqueId().toString()).setValue(TypeToken.of(FactionPlayer.class), factionPlayer);
				} catch (ObjectMappingException e) {
					e.printStackTrace();
				}
    			getPlugin().getPlayerConfig().save();
    		}).async().submit(getPlugin());
        }
        else
        {
        	getPlugin().getPlayersCache().getCache().put(factionPlayer.getUniqueId(), factionPlayer);
    		Task.builder().execute(() -> {
    			try {
    				getPlugin().getPlayerConfig().get().getNode("players", factionPlayer.getUniqueId().toString()).setValue(TypeToken.of(FactionPlayer.class), factionPlayer);
    			} catch (ObjectMappingException e) {
    				e.printStackTrace();
    			}
    			getPlugin().getPlayerConfig().save();
    		}).async().submit(getPlugin());
        }
	}

	@Override
	public void delete(FactionPlayer factionPlayer) {
		getPlugin().getPlayersCache().getCache().remove(factionPlayer.getUniqueId());
		Task.builder().execute(() -> {
			getPlugin().getPlayerConfig().get().getNode("players", factionPlayer.getUniqueId().toString()).setValue(null);
			getPlugin().getPlayerConfig().save();
		}).async().submit(getPlugin());
	}
	
}
