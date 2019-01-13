package io.github.iclickhd.factions.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Faction extends AbstractModel {
	private UUID uniqueId;
	private String name;
	private List<FactionMember> members;
	private Set<String> alliances;
	private Set<String> enemies;
	private Set<FactionClaim> claims;
	
	public Faction(UUID uniqueId, String name) {
		this.uniqueId = uniqueId;
		this.name = name;
		members = new ArrayList<FactionMember>();
		alliances = new HashSet<String>();
		enemies = new HashSet<String>();
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

	public Set<FactionClaim> getClaims() {
		return claims;
	}

	public void setClaims(Set<FactionClaim> claims) {
		this.claims = claims;
	}
}
