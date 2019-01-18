package io.github.iclickhd.factions.configs;

import java.util.List;
import java.util.Optional;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ConfigHelper {
	public static <T> Optional<List<T>> getOptionalList(ConfigurationNode node, TypeToken<T> typeToken) {
		List<T> list = null;
		try {
			list = node.getList(typeToken);
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		
		if(list != null && list.size() > 0) {
			return Optional.of(list);
		}
		
		return Optional.empty();
	}
}
