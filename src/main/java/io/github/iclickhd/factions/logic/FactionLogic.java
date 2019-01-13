package io.github.iclickhd.factions.logic;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.FactionsPlugin;
import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionClaim;
import io.github.iclickhd.factions.models.FactionMember;
import io.github.iclickhd.factions.services.UserService;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class FactionLogic extends AbstractLogic implements ICacheableLogic<Faction> {	
	@Override
	public void addOrUpdate(Faction faction) {
        if (getPlugin().getFactionsCache().getCache().containsKey(faction.getUniqueId()))
        {
        	getPlugin().getFactionsCache().getCache().replace(faction.getUniqueId(), faction);
    		Task.builder().execute(() -> {
    			try {
					getPlugin().getFactionConfig().get().getNode("factions", faction.getUniqueId().toString()).setValue(TypeToken.of(Faction.class), faction);
				} catch (ObjectMappingException e) {
					e.printStackTrace();
				}
    			getPlugin().getFactionConfig().save();
    		}).async().submit(getPlugin());
        }
        else
        {
        	getPlugin().getFactionsCache().getCache().put(faction.getUniqueId(), faction);
    		Task.builder().execute(() -> {
    			try {
    				getPlugin().getFactionConfig().get().getNode("factions", faction.getUniqueId().toString()).setValue(TypeToken.of(Faction.class), faction);
    			} catch (ObjectMappingException e) {
    				e.printStackTrace();
    			}
    			getPlugin().getFactionConfig().save();
    		}).async().submit(getPlugin());
        }
	}

	@Override
	public void delete(Faction faction) {
		getPlugin().getFactionsCache().getCache().remove(faction.getUniqueId());
		Task.builder().execute(() -> {
			getPlugin().getFactionConfig().get().getNode("factions", faction.getUniqueId().toString()).setValue(null);
			getPlugin().getFactionConfig().save();
		}).async().submit(getPlugin());
	}
	
	public Set<Faction> getFactions() {
		return new HashSet<Faction>(FactionsPlugin.getPlugin().getFactionsCache().getCache().values());
	}

	public Optional<Set<FactionMember>> getOnlineMembers(String factionName) {
		Optional<Faction> optionalFaction = getFaction(factionName);
		if(optionalFaction.isPresent()) {
			Faction faction = optionalFaction.get();
			Set<FactionMember> factionMembers = new HashSet<FactionMember>();
			
			for(FactionMember factionMember : faction.getMembers()) {
				if(UserService.isUserOnline(factionMember.getUniqueId())) {
					factionMembers.add(factionMember);
				}
			}
			
			return Optional.of(factionMembers);
		} else {
			return Optional.empty();
		}
	}
	
	public Optional<Faction> getFaction(UUID factionUniqueId) {
		for(Faction faction : getFactions()) {
			if(faction.getUniqueId() == factionUniqueId)
				return Optional.of(faction);
		}
		return Optional.empty();
	}
	
	public Optional<Faction> getFaction(String factionName) {
		for(Faction faction : getFactions()) {
			if(faction.getName().toLowerCase().trim() == factionName.toLowerCase().trim())
				return Optional.of(faction);
		}
		return Optional.empty();
	}
	
	public Optional<Faction> getFactionByPlayerUUID(UUID playerUUID) {
		for(Faction faction : getFactions()) {
			for(FactionMember factionMember : faction.getMembers()) {
				if(factionMember.getUniqueId() == playerUUID)
					return Optional.of(faction);
			}
		}
		return Optional.empty();
	}
	
	public Optional<Faction> getFactionByClaim(World world, Vector3i location) {
		for(Faction faction : getFactions()) {
			for(FactionClaim claim : faction.getClaims()) {
				if(claim.getWorld() == world && claim.getLocation() == location)
					return Optional.of(faction);
			}
		}
		return Optional.empty();
	}
}
