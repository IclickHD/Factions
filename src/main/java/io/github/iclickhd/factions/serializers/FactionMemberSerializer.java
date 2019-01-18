package io.github.iclickhd.factions.serializers;

import java.util.UUID;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.FactionMember;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class FactionMemberSerializer implements TypeSerializer<FactionMember> {
	@Override
	public FactionMember deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		FactionMember factionMember = new FactionMember(value.getNode("uuid").getValue(TypeToken.of(UUID.class)));
		return factionMember;
	}

	@Override
	public void serialize(TypeToken<?> type, FactionMember obj, ConfigurationNode value) throws ObjectMappingException {
		value.getNode("uuid").setValue(TypeToken.of(UUID.class), obj.getUniqueId());
	}
}
