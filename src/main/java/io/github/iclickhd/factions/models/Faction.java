package io.github.iclickhd.factions.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

public class Faction extends AbstractModel {
	private UUID uniqueId;
	private String name;
	private List<FactionMember> members;
	private Set<String> alliances;
	private Set<String> enemies;
	private List<FactionClaim> claims;
	
	public Faction(UUID uniqueId, String name) {
		this.uniqueId = uniqueId;
		this.name = name;
		this.members = new ArrayList<FactionMember>();
		this.alliances = new HashSet<String>();
		this.enemies = new HashSet<String>();
		this.claims = new ArrayList<FactionClaim>();
	}
	
	public UUID getUniqueId() {
		return uniqueId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FactionMember> getMembers() {
		return members;
	}

	public void setMembers(List<FactionMember> members) {
		this.members = members;
	}
	
	public Set<String> getAlliances() {
		return alliances;
	}

	public void setAlliances(Set<String> alliances) {
		this.alliances = alliances;
	}

	public Set<String> getEnemies() {
		return enemies;
	}

	public void setEnemies(Set<String> enemies) {
		this.enemies = enemies;
	}

	public List<FactionClaim> getClaims() {
		return claims;
	}

	public void setClaims(List<FactionClaim> claims) {
		this.claims = claims;
	}
	
	public void addClaim(FactionClaim claim) {
		Sponge.getServer().getConsole().sendMessage(Text.EMPTY);
		Sponge.getServer().getConsole().sendMessage(Text.of(claim.getWorld().getName()));
		Sponge.getServer().getConsole().sendMessage(Text.of(claim.getLocation().toString()));
		Sponge.getServer().getConsole().sendMessage(Text.of(this.claims.size()));
		
		this.claims.add(claim);
	}

	public void removeClaim(FactionClaim claim) {
		this.claims.remove(claim);
	}
}
