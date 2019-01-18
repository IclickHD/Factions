package io.github.iclickhd.factions.serializers;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.FactionClaim;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class FactionClaimSerializer implements TypeSerializer<FactionClaim> {
	@Override
	public FactionClaim deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		World optionalWorld = Sponge.getServer().getWorld(value.getNode("world").getValue(TypeToken.of(UUID.class))).orElse(null);
		FactionClaim factionClaim = new FactionClaim(optionalWorld, value.getNode("location").getValue(TypeToken.of(Vector3i.class)));	
		return factionClaim;
	}

	@Override
	public void serialize(TypeToken<?> type, FactionClaim obj, ConfigurationNode value) throws ObjectMappingException {
		value.getNode("location").setValue(TypeToken.of(Vector3i.class), obj.getLocation());
		value.getNode("world").setValue(TypeToken.of(UUID.class), obj.getWorld().getUniqueId());
	}
}
