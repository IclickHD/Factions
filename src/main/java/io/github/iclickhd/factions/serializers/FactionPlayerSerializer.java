package io.github.iclickhd.factions.serializers;

import java.math.BigDecimal;
import java.util.UUID;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.models.FactionPlayer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class FactionPlayerSerializer implements TypeSerializer<FactionPlayer> {
	@Override
	public FactionPlayer deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		String playerUniqueIdStr = (String) value.getKey();
		FactionPlayer factionPlayer = new FactionPlayer(UUID.fromString(playerUniqueIdStr));
		factionPlayer.setPower(new BigDecimal(value.getNode("power").getString()));
		factionPlayer.setPowerBoost(new BigDecimal(value.getNode("powerboost").getString()));
		return factionPlayer;
	}

	@Override
	public void serialize(TypeToken<?> type, FactionPlayer obj, ConfigurationNode value) throws ObjectMappingException {
		value.getNode("power").setValue(TypeToken.of(BigDecimal.class), obj.getPower());
		value.getNode("powerboost").setValue(TypeToken.of(BigDecimal.class), obj.getPowerBoost());
	}
}
