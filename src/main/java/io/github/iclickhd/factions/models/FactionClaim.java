package io.github.iclickhd.factions.models;

import java.util.UUID;

import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

public class FactionClaim extends AbstractModel {
	private UUID factionUniqueId;
	private World world;
	private Vector3i location;
	
	public FactionClaim(UUID factionUniqueId, World world, Vector3i location) {
		this.factionUniqueId = factionUniqueId;
		this.world = world;
		this.location = location;
	}

	public UUID getFactionUniqueId() {
		return factionUniqueId;
	}

	public void setFactionUniqueId(UUID factionUniqueId) {
		this.factionUniqueId = factionUniqueId;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Vector3i getLocation() {
		return location;
	}

	public void setLocation(Vector3i location) {
		this.location = location;
	}
}
