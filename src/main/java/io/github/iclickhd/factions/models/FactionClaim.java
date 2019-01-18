package io.github.iclickhd.factions.models;

import java.util.List;

import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;

public class FactionClaim extends AbstractModel {
	@SuppressWarnings("serial")
	public static TypeToken<List<FactionClaim>> ListTypeToken = new TypeToken<List<FactionClaim>>() {
	};
	
	private World world;
	private Vector3i location;
	
	public FactionClaim(World world, Vector3i location) {
		this.world = world;
		this.location = location;
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
