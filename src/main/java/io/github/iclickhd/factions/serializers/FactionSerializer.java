package io.github.iclickhd.factions.serializers;

import java.util.List;
import java.util.UUID;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.Faction;
import io.github.iclickhd.factions.models.FactionClaim;
import io.github.iclickhd.factions.models.FactionMember;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class FactionSerializer implements TypeSerializer<Faction> {
	@Override
	public Faction deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		String factionIdStr = (String) value.getKey();
		Faction faction = new Faction(UUID.fromString(factionIdStr), value.getNode("name").getString());
		List<FactionClaim> factionClaims = value.getNode("claims").getList(TypeToken.of(FactionClaim.class));
		List<FactionMember> factionMembers = value.getNode("members").getList(TypeToken.of(FactionMember.class));
		
		if(factionClaims != null && factionClaims.size() > 0) {
			faction.setClaims(factionClaims);
		}
		
		if(factionMembers != null && factionMembers.size() > 0) {
			faction.setMembers(factionMembers);
		}
		
		return faction;
	}

	@Override
	public void serialize(TypeToken<?> type, Faction obj, ConfigurationNode value) throws ObjectMappingException {
		value.getNode("claims").setValue(FactionClaim.ListTypeToken, obj.getClaims());
		value.getNode("members").setValue(FactionMember.ListTypeToken, obj.getMembers());
		value.getNode("name").setValue(obj.getName());
	}
}
