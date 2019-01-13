package io.github.iclickhd.factions.serializers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionMember;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class FactionSerializer implements TypeSerializer<Faction> {
	@Override
	public Faction deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		String factionIdStr = (String) value.getKey();
		Faction faction = new Faction(UUID.fromString(factionIdStr), value.getNode("name").getString());
		List<FactionMember> membersList = value.getNode("members").getList(TypeToken.of(FactionMember.class));
		faction.setMembers(membersList);
		
		return faction;
	}

	@Override
	public void serialize(TypeToken<?> type, Faction obj, ConfigurationNode value) throws ObjectMappingException {
		value.getNode("name").setValue(obj.getName());
		value.getNode("members").setValue(FactionMember.ListTypeToken, new ArrayList<FactionMember>(obj.getMembers()));
	}
}
