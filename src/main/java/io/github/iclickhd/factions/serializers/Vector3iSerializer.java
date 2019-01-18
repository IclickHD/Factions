package io.github.iclickhd.factions.serializers;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class Vector3iSerializer implements TypeSerializer<Vector3i> {
	@Override
	public Vector3i deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		return new Vector3i(value.getNode("x").getInt(), value.getNode("y").getInt(), value.getNode("z").getInt());
	}

	@Override
	public void serialize(TypeToken<?> type, Vector3i obj, ConfigurationNode value) throws ObjectMappingException {
		value.getNode("x").setValue(obj.getX());
		value.getNode("y").setValue(obj.getY());
		value.getNode("z").setValue(obj.getZ());
	}
}
