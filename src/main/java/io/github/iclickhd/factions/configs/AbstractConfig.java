package io.github.iclickhd.factions.configs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.reflect.TypeToken;

import io.github.iclickhd.factions.objects.AbstractPluginObject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public abstract class AbstractConfig extends AbstractPluginObject implements IConfig {
	protected Path configFile;
	protected ConfigurationLoader<CommentedConfigurationNode> configLoader;
	protected CommentedConfigurationNode configNode;
	
	public AbstractConfig(Path configFile) {
		this.configFile = configFile;
		configLoader = HoconConfigurationLoader.builder()
				.setPath(configFile).build();
		setup();
	}
	
	public void setup() {
		if (!Files.exists(configFile)) {
			try {
				Files.createFile(configFile);
				load();
				populate();
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			load();
		}
	}
	
	public void load() {
		try {
			configNode = configLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			configLoader.save(configNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CommentedConfigurationNode get() {
		return configNode;
	}
	
	public Path getConfigFile() {
		return configFile;
	}
	
	public void setValueAndSave(Object value, Object... path) {
		get().getNode(path).setValue(value);
		save();
	}
	
	public <T> void setValueAndSave(T value, TypeToken<T> type, Object... path) throws ObjectMappingException {
		get().getNode(path).setValue(type, value);
		save();
	}
	
	public abstract void populate();
}
