package io.github.iclickhd.factions.logic;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.FactionsPlugin;
import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionClaim;
import io.github.iclickhd.factions.models.FactionMember;
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
				if(getPlugin().getUserManager().isUserOnline(factionMember.getUniqueId())) {
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
			if(faction.getUniqueId().equals(factionUniqueId))
				return Optional.of(faction);
		}
		return Optional.empty();
	}
	
	public Optional<Faction> getFaction(String factionName) {
		for(Faction faction : getFactions()) {
			if(faction.getName().toLowerCase().trim().equals(factionName.toLowerCase().trim()))
				return Optional.of(faction);
		}
		return Optional.empty();
	}
	
	public Optional<Faction> getFactionByPlayerUUID(UUID playerUUID) {
		for(Faction faction : getFactions()) {
			for(FactionMember factionMember : faction.getMembers()) {
				if(factionMember.getUniqueId().equals(playerUUID))
					return Optional.of(faction);
			}
		}
		return Optional.empty();
	}
	
	public Optional<Faction> getFactionByPlayer(Player player) {
		return getFactionByPlayerUUID(player.getUniqueId());
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
	
	public BigDecimal getFactionPower(UUID factionUUID) {
		BigDecimal power = BigDecimal.ZERO;
		Optional<Faction> optionalFaction = getFaction(factionUUID);
		if(optionalFaction.isPresent()) {
			Faction faction = optionalFaction.get();
			
			for(FactionMember factionMember : faction.getMembers()) {
				power = power.add(getPlugin().getPowerManager().getPower(factionMember.getUniqueId()));
			}
		}
		return power;
	}
	
	public BigDecimal getFactionMaxPower(UUID factionUUID) {
		BigDecimal power = BigDecimal.ZERO;
		Optional<Faction> optionalFaction = getFaction(factionUUID);
		if(optionalFaction.isPresent()) {
			Faction faction = optionalFaction.get();
			for(FactionMember factionMember : faction.getMembers()) {
				power = power.add(getPlugin().getPowerManager().getMaxPower(factionMember.getUniqueId()));
			}
		}
		return power;
	}
	
	public int getFactionRoundedMaxPower(UUID factionUUID) {
		return getFactionMaxPower(factionUUID).setScale(0, BigDecimal.ROUND_DOWN).intValueExact();
	}
	
	public int getFactionRoundedPower(UUID factionUUID) {
		return getFactionPower(factionUUID).setScale(0, BigDecimal.ROUND_DOWN).intValueExact();
	}
	
	public void claimChunk(Faction faction, World world, Vector3i location) {
		faction.addClaim(new FactionClaim(world, location));
		addOrUpdate(faction);
	}
	
	public boolean canClaim(Faction faction) {
		return faction.getClaims().size() < getFactionRoundedPower(faction.getUniqueId());
	}
	
	public boolean canOverClaim(Faction faction, Faction chunkFaction) {
		return canClaim(faction) && chunkFaction.getClaims().size() > getFactionRoundedPower(chunkFaction.getUniqueId());
	}
}
