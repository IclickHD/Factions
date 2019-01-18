package io.github.iclickhd.factions.serializers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.configs.ConfigHelper;
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
		
		Optional<List<FactionMember>> optionalFactionMembers = ConfigHelper.getOptionalList(value.getNode("members"), TypeToken.of(FactionMember.class));
		Optional<List<FactionClaim>> optionalFactionClaims = ConfigHelper.getOptionalList(value.getNode("claims"), TypeToken.of(FactionClaim.class));
		
		if(optionalFactionMembers.isPresent()) {
			faction.setMembers(optionalFactionMembers.get());
		}
		
		if(optionalFactionClaims.isPresent()) {
			faction.setClaims(optionalFactionClaims.get());
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
